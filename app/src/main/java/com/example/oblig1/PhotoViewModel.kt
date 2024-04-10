package com.example.oblig1

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * ViewModel for the preserving state between screen rotations
 * @param repository PhotoRepository instance
 */
class PhotoViewModel(private val repository: PhotoRepository) : ViewModel() {

    // keep track of current score
    var score = 0

    // keep track of total attempts
    var totalScore = 0

    // keep track of currently displayed photo
    var currentPhoto: PhotoDescription? = null

    // keep track of answer options
    var buttonDescriptions: Array<String> = arrayOf()

    // Using LiveData and caching what allPhotos returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allPhotos: LiveData<List<PhotoDescription>> = repository.allPhotos.asLiveData()
    val allDescriptions: LiveData<List<String>> = repository.allDescriptions.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     * @param photo PhotoDescription photo to insert
     */
    fun insert(photo: PhotoDescription) = viewModelScope.launch {
        repository.insert(photo)
    }


    /**
     * Launching a new coroutine to delete the data in a non-blocking way
     * @param photo PhotoDescription photo to delete
     */
    fun delete(photo: PhotoDescription) = viewModelScope.launch {
        repository.delete(photo)
    }


}

/**
 * This class is used for creating PhotoViewModel instance
 * @param repository PhotoRepository instance
 */
class PhotoViewModelFactory(private val repository: PhotoRepository) : ViewModelProvider.Factory {
    /**
     * This method is used to create a new instance of the given ViewModel class
     * @param modelClass Class<T> class to create instance of
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Check if the model class is PhotoViewModel
        if (modelClass.isAssignableFrom(PhotoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return PhotoViewModel(repository) as T
        }
        // If the model class is not PhotoViewModel, throw an exception
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}