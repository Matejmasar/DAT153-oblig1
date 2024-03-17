package com.example.oblig1

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class PhotoViewModel(private val repository: PhotoRepository): ViewModel() {

    // Using LiveData and caching what allPhotos returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allPhotos: LiveData<List<PhotoDescription>> = repository.allPhotos.asLiveData()
    val allDescriptions: LiveData<List<String>> = repository.allDescriptions.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(photo: PhotoDescription) = viewModelScope.launch {
        repository.insert(photo)
    }

    fun delete(photo: PhotoDescription) = viewModelScope.launch {
        repository.delete(photo)
    }


}

class PhotoViewModelFactory(private val repository: PhotoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PhotoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}