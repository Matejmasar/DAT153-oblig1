package com.example.oblig1

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * This class keeps global state of the application
 */
class PhotosApplication : Application() {

    // CoroutineScope is used to launch coroutines, SupervisorJob ensures that failing child doesn't affect others
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { PhotoRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { PhotoRepository(database.photoDao()) }

}