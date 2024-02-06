package com.example.oblig1

import android.os.Parcel
import android.os.Parcelable

/**
 * This class is made for storing data
 * it stores source for the image and its description
 * In order to be added to Intent, it needs to be Parcelable
 */
data class PhotoDescription(val photo: Any, val description: String) : Parcelable {
    // reconstruct from parcel
    constructor(parcel: Parcel) : this(
        parcel.readValue(Any::class.java.classLoader) ?: "",
        parcel.readString() ?: ""
    )

    // fold into parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(photo)
        parcel.writeString(description)
    }

    // no real use
    override fun describeContents(): Int {
        return 0
    }

    // companion to comply with Parcelable
    companion object CREATOR : Parcelable.Creator<PhotoDescription> {
        override fun createFromParcel(parcel: Parcel): PhotoDescription {
            return PhotoDescription(parcel)
        }

        override fun newArray(size: Int): Array<PhotoDescription?> {
            return arrayOfNulls(size)
        }
    }
}
