package com.example.oblig1

import androidx.room.TypeConverter
import android.net.Uri

/**
 * This class is made for converting Uri to String and vice versa
 */
class UriConverter {

    // Convert Uri to String
    @TypeConverter
    fun fromUri(uri: Uri): String {
        return uri.toString()
    }

    // Convert String to Uri
    @TypeConverter
    fun toUri(uriString: String): Uri {
        return Uri.parse(uriString)
    }
}