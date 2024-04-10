package com.example.oblig1

import android.content.Context
import android.net.Uri
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * This class is used to create the Room database
 */
@Database(entities = [PhotoDescription::class], version = 2, exportSchema = false)
// Use the UriConverter class to convert Uri to String and vice versa
@TypeConverters(UriConverter::class)
abstract class PhotoRoomDatabase : RoomDatabase() {
    // Abstract method to get the PhotoDao
    abstract fun photoDao(): PhotoDao

    // Singleton prevents multiple instances of database opening at the same time
    companion object {
        // singleton instance of the database
        @Volatile
        private var INSTANCE: PhotoRoomDatabase? = null

        /**
         * This method is used to get the database instance
         * @param context Context of the application
         * @param scope CoroutineScope
         * @return PhotoRoomDatabase instance
         */
        fun getDatabase(context: Context, scope: CoroutineScope): PhotoRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, PhotoRoomDatabase::class.java, "photo_database"
                ).addCallback(PhotoDatabaseCallback(scope)).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    /**
     * This class is used to populate the database with default photos
     */
    private class PhotoDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        /**
         * This method is called when the database is created
         * @param db SupportSQLiteDatabase instance
         */
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.photoDao())
                }
            }
        }

        /**
         * This method is called when the database is migrated
         * @param db SupportSQLiteDatabase instance
         */
        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
            super.onDestructiveMigration(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.photoDao())
                }
            }
        }

        /**
         * This method is used to convert resource id to Uri
         * @param resourceId Int resource to convert
         */
        private fun resourceIdToUri(resourceId: Int): Uri {
            return Uri.parse("android.resource://com.example.oblig1/$resourceId")
        }

        /**
         * This method is used to populate the database with default photos
         * @param photoDao PhotoDao instance
         */
        suspend fun populateDatabase(photoDao: PhotoDao) {
            photoDao.insert(
                PhotoDescription(
                    photo = resourceIdToUri(R.drawable.cat), description = "Cat"
                )
            )
            photoDao.insert(
                PhotoDescription(
                    photo = resourceIdToUri(R.drawable.dog), description = "Dog"
                )
            )
            photoDao.insert(
                PhotoDescription(
                    photo = resourceIdToUri(R.drawable.lion), description = "Lion"
                )
            )
        }


    }
}