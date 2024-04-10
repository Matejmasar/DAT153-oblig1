package com.example.oblig1

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This class represents the PhotoDescription entity and is used to store the photo and its description,
 * Used for ORM with Room
 */
@Entity(tableName = "photos")
data class PhotoDescription(
    // Primary key with auto increment, unique identifier for each photo
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    // URI of the photo
    @ColumnInfo(name = "uri") val photo: Uri,
    // Description of the photo
    @ColumnInfo(name = "description") val description: String
) {

}