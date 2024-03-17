package com.example.oblig1

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
// generate methods for database operations

    @Query("SELECT * FROM photos")
    fun getAllPhotos(): Flow<List<PhotoDescription>>

    @Insert
    suspend fun insert(photo: PhotoDescription)

    @Delete
    suspend fun delete(photo: PhotoDescription)

    // get all descriptions
    @Query("SELECT description FROM photos")
    fun getAllDescriptions(): Flow<List<String>>

}