package com.example.oblig1

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * This DAO (Data Access Object) interface is used to define the methods that access the database
 */
@Dao
interface PhotoDao {

    /**
     * This method is used to get all photos from the database
     * @return Flow<List<PhotoDescription>> Flow of list of PhotoDescription photos
     */
    @Query("SELECT * FROM photos")
    fun getAllPhotos(): Flow<List<PhotoDescription>>

    /**
     * This method is used to insert a photo into the database
     * @param photo PhotoDescription photo to insert
     */
    @Insert
    suspend fun insert(photo: PhotoDescription)

    /**
     * This method is used to delete a photo from the database
     * @param photo PhotoDescription photo to delete
     */
    @Delete
    suspend fun delete(photo: PhotoDescription)

    /**
     * This method is used to get all descriptions from the database
     * @return Flow<List<String>> Flow of list of String descriptions
     */
    @Query("SELECT description FROM photos")
    fun getAllDescriptions(): Flow<List<String>>

}