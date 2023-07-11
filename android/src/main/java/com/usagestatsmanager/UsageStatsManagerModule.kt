package com.usagestatsmanager

import android.annotation.TargetApi
import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Process;
import android.os.RemoteException
import android.util.Log
import androidx.annotation.RequiresApi
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableNativeMap
import com.facebook.react.common.MapBuilder

class UsageStatsManagerModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {
  private var reactContext: ReactApplicationContext? = reactContext

  @RequiresApi(Build.VERSION_CODES.M)
  private var networkStatsManager: NetworkStatsManager? = reactContext.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager

  override fun getName(): String {
    return NAME
  }

  override  fun getConstants(): kotlin.collections.Map<String, Any>? {
    val constants: MutableMap<String, Any> = MapBuilder.newHashMap()
    constants["INTERVAL_WEEKLY"] = UsageStatsManager.INTERVAL_WEEKLY
    constants["INTERVAL_MONTHLY"] = UsageStatsManager.INTERVAL_MONTHLY
    constants["INTERVAL_YEARLY"] = UsageStatsManager.INTERVAL_YEARLY
    constants["INTERVAL_DAILY"] = UsageStatsManager.INTERVAL_DAILY
    constants["INTERVAL_BEST"] = UsageStatsManager.INTERVAL_BEST
    constants["TYPE_WIFI"] = ConnectivityManager.TYPE_WIFI
    constants["TYPE_MOBILE"] = ConnectivityManager.TYPE_MOBILE
    constants["TYPE_MOBILE_AND_WIFI"] = Int.MAX_VALUE
    return constants
  }

  private fun packageExists(packageName: String): Boolean {
    val packageManager: PackageManager? = reactContext?.getPackageManager()
    var info: ApplicationInfo? = null
    info = try {
      packageManager?.getApplicationInfo(packageName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
      e.printStackTrace()
      return false
    }
    return true
  }

  @ReactMethod(isBlockingSynchronousMethod = true)
  fun showUsageAccessSettings(packageName: String) {
    val intent = Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS)
    if (packageExists(packageName)) {
      intent.setData(Uri.fromParts("package", packageName, null))
    }
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    reactContext?.startActivity(intent)
  }

  @ReactMethod
  fun queryUsageStats(interval: Int, startTime: Double, endTime: Double, promise: Promise) {

    val result: WritableMap = WritableNativeMap()
    val usageStatsManager: UsageStatsManager =
      reactContext?.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    val time = System.currentTimeMillis()
    val queryUsageStats: MutableList<UsageStats>? =
      usageStatsManager.queryUsageStats(interval, startTime.toLong(), endTime.toLong())
    if (queryUsageStats != null) {
      for (us in queryUsageStats) {
        if(us.totalTimeInForeground.toInt() !== 0) {
          Log.d("UsageStats", us.packageName + " = " + us.totalTimeInForeground)
          val usageStats: WritableMap = WritableNativeMap()
          usageStats.putString("packageName", us.packageName)
          val totalTimeInSeconds = us.totalTimeInForeground.toDouble() / 1000
          usageStats.putDouble("totalTimeInForeground", totalTimeInSeconds)
          usageStats.putDouble("firstTimeStamp", us.firstTimeStamp.toDouble())
          usageStats.putDouble("lastTimeStamp", us.lastTimeStamp.toDouble())
          usageStats.putDouble("lastTimeUsed", us.lastTimeUsed.toDouble())
          usageStats.putInt("describeContents", us.describeContents())
          result.putMap(us.getPackageName(), usageStats)
        }
      }
    }
    promise.resolve(result)
  }

  @ReactMethod
  fun checkForPermission(promise: Promise) {
    val appOps: AppOpsManager = reactContext?.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode: Int =
      appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, Process.myUid(), reactContext?.getPackageName()!!)
    promise.resolve(mode == MODE_ALLOWED)
  }

  @TargetApi(Build.VERSION_CODES.M)
  private fun getDataUsage(
    networkType: Int,
    subscriberId: String?,
    packageUid: Int,
    startTime: Long,
    endTime: Long
  ): Double {
    val networkStatsByApp: NetworkStats
    var currentDataUsage = 0.0
    try {
      networkStatsByApp = networkStatsManager?.querySummary(networkType, subscriberId, startTime, endTime)!!
      do {
        val bucket: NetworkStats.Bucket = NetworkStats.Bucket()
        networkStatsByApp.getNextBucket(bucket)
        if (bucket.getUid() === packageUid) {
          currentDataUsage += bucket.rxBytes + bucket.txBytes
        }
      } while (networkStatsByApp.hasNextBucket())
    } catch (e: RemoteException) {
      e.printStackTrace()
    }
    return currentDataUsage
  }

  @ReactMethod
  fun getAppDataUsage(packageName: String, networkType: Int, startTime: Double, endTime: Double, promise: Promise) {
    // get sim card
    val uid = getAppUid(packageName)
    if (networkType == ConnectivityManager.TYPE_MOBILE) {
      promise.resolve(getDataUsage(ConnectivityManager.TYPE_MOBILE, null, uid, startTime.toLong(), endTime.toLong()))
    } else if (networkType == ConnectivityManager.TYPE_WIFI) {
      promise.resolve(getDataUsage(ConnectivityManager.TYPE_WIFI, "", uid, startTime.toLong(), endTime.toLong()))
    } else {
      promise.resolve(
        getDataUsage(ConnectivityManager.TYPE_MOBILE, "", uid, startTime.toLong(), endTime.toLong())
          + getDataUsage(ConnectivityManager.TYPE_WIFI, "", uid, startTime.toLong(), endTime.toLong())
      )
    }
  }

  private fun getAppUid(packageName: String): Int {
    // get app uid
    val packageManager: PackageManager? = reactContext?.getPackageManager()
    var info: ApplicationInfo? = null
    try {
      info = packageManager?.getApplicationInfo(packageName, 0)
    } catch (e: PackageManager.NameNotFoundException) {
      e.printStackTrace()
    }
    var uid = 0
    if (info != null) {
      uid = info.uid
    }
    return uid
  }

  companion object {
    const val NAME = "UsageStatsManager"
  }
}
