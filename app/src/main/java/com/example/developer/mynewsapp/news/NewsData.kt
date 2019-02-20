package com.example.developer.mynewsapp.news

import android.os.Parcel
import android.os.Parcelable

class NewsData(val name: String = "", val text: String = "", val date: String = "",
               val imageUrl: String = "", val important: Boolean = false) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(text)
        parcel.writeString(date)
        parcel.writeString(imageUrl)
        parcel.writeByte(if (important) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NewsData> {
        override fun createFromParcel(parcel: Parcel): NewsData {
            return NewsData(parcel)
        }

        override fun newArray(size: Int): Array<NewsData?> {
            return arrayOfNulls(size)
        }
    }
}