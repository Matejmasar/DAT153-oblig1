package com.example.oblig1

import android.app.Application
import android.net.Uri

/**
 * This class is made for storing data, used before transition to Room
 */
class DataManager : Application() {

    // Singleton pattern, only one instance of DataManager
    companion object {
        lateinit var instance: DataManager
            private set
    }

    // keep track of current score
    private var score: Int = 0

    // keep track of total attempts
    private var total: Int = 0

    // keep track of all photos
    private var allPhotos: ArrayList<PhotoDescription>? = ArrayList()

    /**
     * Return the current score
     * @return Int score
     */
    fun getScore(): Int {
        return score
    }

    /**
     * Return the total attempts
     * @return Int total attempts
     */
    fun getTotal(): Int {
        return total
    }

    /**
     * Return all photos
     * @return ArrayList<PhotoDescription> all photos
     */
    fun getAllPhotos(): ArrayList<PhotoDescription>? {
        return allPhotos
    }

    /**
     * Add a photo to the list
     * @param photo PhotoDescription photo to add
     */
    fun addPhoto(photo: PhotoDescription) {
        allPhotos?.add(photo)
    }

    /**
     * Remove a photo from the list
     * @param position Int position of the photo to remove
     */
    fun removePhoto(position: Int) {
        allPhotos?.removeAt(position)
    }

    /**
     * Convert resource id to Uri
     * @param resourceId Int resource to convert
     */
    private fun resourceIdToUri(resourceId: Int): Uri {
        return Uri.parse("android.resource://com.example.oblig1/$resourceId")
    }

    override fun onCreate() {
        super.onCreate()
        // initialize DataManager
        instance = this
        // add default photos from Resource folder
        allPhotos?.add(
            PhotoDescription(
                photo = resourceIdToUri(R.drawable.cat), description = "Cat"
            )
        )
        allPhotos?.add(
            PhotoDescription(
                photo = resourceIdToUri(R.drawable.dog), description = "Dog"
            )
        )
        allPhotos?.add(
            PhotoDescription(
                photo = resourceIdToUri(R.drawable.lion), description = "Lion"
            )
        )
    }

    /**
     * Increase the score
     */
    fun increaseScore() {
        score++
    }

    /**
     * Increase the total attempts
     */
    fun increaseTotal() {
        total++
    }

}