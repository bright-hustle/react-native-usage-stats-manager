package com.usagestatsmanager.model

import android.os.Parcel
import android.os.Parcelable
import java.util.Locale

/**
 * Created by CodeSyncr
 *
 * @CodeSyncr yashkumar12125@gmail.com
 */
class AppData : Parcelable {
  var mName: String? = null
  var mPackageName: String? = null
  var mEventTime: Long = 0
  var mUsageTime: Long = 0
  var mEventType = 0
  var mCount = 0
  var mMobile: Long = 0
  var mWifi: Long = 0
  var mCanOpen = false
  var mIsSystem = false

  constructor() {}
  protected constructor(`in`: Parcel) {
    mName = `in`.readString()
    mPackageName = `in`.readString()
    mEventTime = `in`.readLong()
    mUsageTime = `in`.readLong()
    mEventType = `in`.readInt()
    mCount = `in`.readInt()
    mMobile = `in`.readLong()
    mWifi = `in`.readLong()
    mCanOpen = `in`.readByte().toInt() != 0
    mIsSystem = `in`.readByte().toInt() != 0
  }

  override fun toString(): String {
    return String.format(
      Locale.getDefault(),
      "name:%s package_name:%s time:%d total:%d type:%d system:%b count:%d",
      mName, mPackageName, mEventTime, mUsageTime, mEventType, mIsSystem, mCount
    )
  }

  fun copy(): AppData {
    val newItem = AppData()
    newItem.mName = mName
    newItem.mPackageName = mPackageName
    newItem.mEventTime = mEventTime
    newItem.mUsageTime = mUsageTime
    newItem.mEventType = mEventType
    newItem.mIsSystem = mIsSystem
    newItem.mCount = mCount
    return newItem
  }

  override fun describeContents(): Int {
    return 0
  }

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeString(mName)
    dest.writeString(mPackageName)
    dest.writeLong(mEventTime)
    dest.writeLong(mUsageTime)
    dest.writeInt(mEventType)
    dest.writeInt(mCount)
    dest.writeLong(mMobile)
    dest.writeLong(mWifi)
    dest.writeByte((if (mCanOpen) 1 else 0).toByte())
    dest.writeByte((if (mIsSystem) 1 else 0).toByte())
  }

//  companion object {
//    val CREATOR: Parcelable.Creator<AppData?> = object : Parcelable.Creator<AppData?> {
//      override fun createFromParcel(`in`: Parcel): AppData? {
//        return AppData(`in`)
//      }
//
//      override fun newArray(size: Int): Array<AppData?> {
//        return arrayOfNulls(size)
//      }
//    }
//  }

  companion object CREATOR : Parcelable.Creator<AppData> {
    override fun createFromParcel(parcel: Parcel): AppData {
      return AppData(parcel)
    }

    override fun newArray(size: Int): Array<AppData?> {
      return arrayOfNulls(size)
    }
  }
}
