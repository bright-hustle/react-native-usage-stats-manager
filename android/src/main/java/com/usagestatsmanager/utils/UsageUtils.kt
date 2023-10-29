package com.usagestatsmanager.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import java.util.Calendar
import java.util.Locale

object UsageUtils {

    const val USAGE_TIME_MIX = 5000L

    fun humanReadableMillis(milliSeconds: Long): String {
        val seconds = milliSeconds / 1000
        return when {
            seconds < 60 -> "${seconds}s"
            seconds < 3600 -> "${seconds / 60}m ${seconds % 60}s"
            else -> "${seconds / 3600}h ${(seconds % 3600) / 60}m ${seconds % 60}s"
        }
    }

    fun humanReadableByteCount(bytes: Long): String {
        val unit = 1024
        if (bytes < unit) return "$bytes B"
        val exp = (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
        val pre = "KMGTPE"[exp - 1].toString()
        return String.format(
                Locale.getDefault(),
                "%.1f %sB",
                bytes / Math.pow(unit.toDouble(), exp.toDouble()),
                pre
        )
    }

    fun openable(packageManager: PackageManager, packageName: String): Boolean {
        return packageManager.getLaunchIntentForPackage(packageName) != null
    }

    fun isSystemApp(manager: PackageManager, packageName: String): Boolean {
        var isSystemApp = false
        try {
            val applicationInfo = manager.getApplicationInfo(packageName, 0)
            isSystemApp =
                    (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0) ||
                            (applicationInfo.flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return isSystemApp
    }

    fun isInstalled(packageManager: PackageManager, packageName: String): Boolean {
        var applicationInfo: ApplicationInfo? = null
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return applicationInfo != null
    }

     fun parsePackageIcon(packageName: String, defaultIcon: Int, context: Context): Drawable {
         val manager = context.packageManager
         return try {
             manager.getApplicationIcon(packageName)
         } catch (e: PackageManager.NameNotFoundException) {
             e.printStackTrace()
           context.resources.getDrawable(defaultIcon)
         }
     }

    fun parsePackageName(pckManager: PackageManager, data: String): CharSequence {
        var applicationInformation: ApplicationInfo?
        try {
            applicationInformation =
                    pckManager.getApplicationInfo(data, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            applicationInformation = null
        }
      if(applicationInformation == null){
        return getAppName(data)
      }
        val appLabel = applicationInformation?.loadLabel(pckManager)
        return appLabel?.takeIf { it.isNotBlank() } ?: data
//        return applicationInformation?.let { pckManager.getApplicationLabel(it) } ?: data
    }

    fun getAppName(packageName: String): String {
      when (packageName) {
        "com.brighthustle.ark" -> return "Ark"
        "com.android.chrome" -> return "Google Chrome"
        "com.whatsapp" -> return "WhatsApp"
        "com.google.android.apps.photos" -> return "Google Photos"
        "com.facebook.katana" -> return "Facebook"
        "com.instagram.android" -> return "Instagram"
        "com.twitter.android" -> return "Twitter"
        "com.google.android.youtube" -> return "YouTube"
        "com.google.android.maps" -> return "Google Maps"
        "com.google.android.gm" -> return "Gmail"
        "com.spotify.music" -> return "Spotify"
        "com.netflix.mediaclient" -> return "Netflix"
        "com.microsoft.office.word" -> return "Microsoft Word"
        "com.microsoft.office.excel" -> return "Microsoft Excel"
        "com.adobe.reader" -> return "Adobe Acrobat Reader"
        "com.dropbox.android" -> return "Dropbox"
        "com.evernote" -> return "Evernote"
        "com.linkedin.android" -> return "LinkedIn"
        "com.pinterest" -> return "Pinterest"
        "com.tinder" -> return "Tinder"
        "com.mojang.minecraftpe" -> return "Minecraft"
        "com.tencent.ig" -> return "PUBG Mobile"
        "com.epicgames.fortnite" -> return "Fortnite"
        "com.king.candycrushsaga" -> return "Candy Crush Saga"
        "com.supercell.clashofclans" -> return "Clash of Clans"
        "com.dts.freefireth" -> return "Garena Free Fire"
        "com.kiloo.subwaysurf" -> return "Subway Surfers"
        "com.activision.callofduty.shooter" -> return "Call of Duty: Mobile"
        "com.innersloth.spacemafia" -> return "Among Us"
        "com.roblox.client" -> return "Roblox"
        "com.ea.games.simsfreeplay_row" -> return "The Sims FreePlay"
        "com.netmarble.koongya101" -> return "LINE Rangers"
        "com.rockstargames.gtasa" -> return "Grand Theft Auto: San Andreas"
        "com.ubisoft.assassinscreed.identity" -> return "Assassin's Creed Identity"
        "com.playrix.gardenscapes" -> return "Gardenscapes"
        "com.madfingergames.deadtrigger2" -> return "Dead Trigger 2"
        "com.squareenixmontreal.lcgo" -> return "Lara Croft: Relic Run"
        "com.scopely.wheeloffortune" -> return "Wheel of Fortune Free Play"
        "com.kabam.marvelbattle" -> return "MARVEL Contest of Champions"
        "com.dream11.android" -> return "Dream11"
        "com.my11circle.app" -> return "My11Circle"
        "com.fantain.fantain" -> return "Fantain"
        "com.myteam11.app" -> return "MyTeam11"
        "com.ballebaazi.app" -> return "BalleBaazi"
        "com.hala.play" -> return "HalaPlay"
        "com.cricplay" -> return "CricPlay"
        "com.mpl.androidapp" -> return "Mobile Premier League (MPL)"
        "com.google.android.apps.nbu.paisa.user" -> return "Google Pay"
        "com.phonepe.app" -> return "PhonePe"
        "com.amazon.mShop.android.shopping" -> return "Amazon"
        "com.paytm.app" -> return "Paytm"
        "com.axis.mobile" -> return "Axis Mobile"
        "com.sbi.lotusintouch" -> return "SBI Anywhere Personal"
        "com.mobikwik_new" -> return "MobiKwik"
        "com.freecharge.android" -> return "FreeCharge"
        "com.airtel.myrTselMob" -> return "Airtel Thanks"
        "com.microsoft.office.outlook" -> return "Microsoft Outlook"
        "in.startv.hotstar" -> return "Disney+ Hotstar"
        "com.hbo.hbonow" -> return "HBO Max"
        "com.amazon.avod.thirdpartyclient" -> return "Amazon Prime Video"
        "com.disney.disneyplus" -> return "Disney+"
        "com.vudu.android" -> return "Vudu"
        "com.zee5.z5" -> return "ZEE5"
        "com.sonyliv" -> return "SonyLIV"
        "com.mxtech.videoplayer.ad" -> return "MX Player"
        "com.alimuzaffar.libusb" -> return "USB OTG Checker"
        "com.theplatform.pdk" -> return "Xfinity Stream"
        else -> return packageName
      }
    }

    fun getAppUid(packageManager: PackageManager, packageName: String): Int {
        val applicationInfo: ApplicationInfo
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            return applicationInfo.uid
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }

    fun getTimeRange(sort: SortOrder): LongArray {
        val range: LongArray
        when (sort) {
            SortOrder.TODAY -> range = getTodayRange()
            SortOrder.YESTERDAY -> range = getYesterday()
            SortOrder.THIS_WEEK -> range = getThisWeek()
            SortOrder.MONTH -> range = getThisMonth()
            SortOrder.THIS_YEAR -> range = getThisYear()
            else -> range = getTodayRange()
        }
        return range
    }

    private const val A_DAY = 86400 * 1000L

    private fun getTodayRange(): LongArray {
        val timeNow = System.currentTimeMillis()
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return longArrayOf(cal.timeInMillis, timeNow)
    }

    private fun getYesterday(): LongArray {
        val timeNow = System.currentTimeMillis()
        val cal = Calendar.getInstance()
        cal.timeInMillis = timeNow - A_DAY
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        val end = if (start + A_DAY > timeNow) timeNow else start + A_DAY
        return longArrayOf(start, end)
    }

    private fun getThisWeek(): LongArray {
        val timeNow = System.currentTimeMillis()
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        val end = if (start + A_DAY > timeNow) timeNow else start + A_DAY
        return longArrayOf(start, end)
    }

    private fun getThisMonth(): LongArray {
        val timeNow = System.currentTimeMillis()
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return longArrayOf(cal.timeInMillis, timeNow)
    }

    private fun getThisYear(): LongArray {
        val timeNow = System.currentTimeMillis()
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR))
        cal.set(Calendar.MONTH, Calendar.JANUARY)
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return longArrayOf(cal.timeInMillis, timeNow)
    }
}
