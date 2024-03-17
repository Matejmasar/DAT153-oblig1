package com.example.oblig1

import android.net.Uri
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This class is made for storing data
 * it stores source for the image and its description
 */
@Entity(tableName = "photos")
data class PhotoDescription(@PrimaryKey @ColumnInfo(name = "uri") val photo: Uri,
                       @ColumnInfo(name = "description") val description: String){

}