package com.example.oblig1

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class PhotoRepository(private val photoDao: PhotoDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allPhotos: Flow<List<PhotoDescription>> = photoDao.getAllPhotos()
    val allDescriptions: Flow<List<String>> = photoDao.getAllDescriptions()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @WorkerThread
    suspend fun insert(photo: PhotoDescription) {
        photoDao.insert(photo)
    }

    @WorkerThread
    suspend fun delete(photo: PhotoDescription) {
        photoDao.delete(photo)
    }



}