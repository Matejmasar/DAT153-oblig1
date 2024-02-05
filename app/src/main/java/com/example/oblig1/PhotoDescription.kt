package com.example.oblig1

import android.os.Parcel
import android.os.Parcelable

data class PhotoDescription(val photo: Any, val description: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Any::class.java.classLoader) ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(photo)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoDescription> {
        override fun createFromParcel(parcel: Parcel): PhotoDescription {
            return PhotoDescription(parcel)
        }

        override fun newArray(size: Int): Array<PhotoDescription?> {
            return arrayOfNulls(size)
        }
    }
}
