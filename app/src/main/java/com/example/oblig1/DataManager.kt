package com.example.oblig1

import android.app.Application
import android.net.Uri

class DataManager : Application() {
    companion object {
        lateinit var instance: DataManager
            private set
    }

    private var score: Int = 0
    private var total: Int = 0
    private var allPhotos: ArrayList<PhotoDescription>? = ArrayList()

    fun getScore(): Int {
        return score
    }


    fun getTotal(): Int {
        return total
    }


    fun getAllPhotos(): ArrayList<PhotoDescription>? {
        return allPhotos
    }

    fun addPhoto(photo: PhotoDescription) {
        allPhotos?.add(photo)
    }

    fun removePhoto(position: Int){
        allPhotos?.removeAt(position)
    }

     private fun resourceIdToUri(resourceId: Int): Uri {
        return Uri.parse("android.resource://com.example.oblig1/$resourceId")
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        allPhotos?.add(PhotoDescription(resourceIdToUri(R.drawable.cat), "Cat"))
        allPhotos?.add(PhotoDescription(resourceIdToUri(R.drawable.dog), "Dog"))
        allPhotos?.add(PhotoDescription(resourceIdToUri(R.drawable.lion), "Lion"))
    }

    fun increaseScore() {
        score++
    }

    fun increaseTotal() {
        total++
    }

}