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


@Database(entities = [PhotoDescription::class], version = 1, exportSchema = false)
@TypeConverters(UriConverter::class)
abstract class PhotoRoomDatabase : RoomDatabase()
{
    abstract fun photoDao(): PhotoDao


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: PhotoRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): PhotoRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PhotoRoomDatabase::class.java,
                    "photo_database"
                ).addCallback(PhotoDatabaseCallback(scope))
                 .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    private class PhotoDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.photoDao())
                }
            }
        }

        private fun resourceIdToUri(resourceId: Int): Uri {
            return Uri.parse("android.resource://com.example.oblig1/$resourceId")
        }

        suspend fun populateDatabase(photoDao: PhotoDao) {
            photoDao.insert(PhotoDescription(resourceIdToUri(R.drawable.cat), "Cat"))
            photoDao.insert(PhotoDescription(resourceIdToUri(R.drawable.dog), "Dog"))
            photoDao.insert(PhotoDescription(resourceIdToUri(R.drawable.lion), "Lion"))
        }


    }
}