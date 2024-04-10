package com.example.oblig1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * This class represents the Gallery, with its photos, descriptions, button to add new photos,
 * handling deleting photos, etc.
 */
class GalleryActivity : AppCompatActivity() {

    // view model for photos
    private val photoViewModel: PhotoViewModel by viewModels {
        PhotoViewModelFactory((application as PhotosApplication).repository)
    }

    // adapter to populate photos into UI
    private lateinit var galleryAdapter: GalleryAdapter

    // remembering current sorting value
    private var currentSortSelection: String = "Select sorting"


    /**
     * This method is called when the activity is created
     * @param savedInstanceState Bundle saved state of the activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        // draw photos onto UI
        drawPhotos()
        // populate spinner with sorting options
        populateSpinner()
        // set up Floating Action Button to add new photos
        setUpFAB()

        // Observe the LiveData from the ViewModel
        photoViewModel.allPhotos.observe(this) { photos ->
            // Update the cached copy of the photos in the adapter.
            photos?.let {
                val sortedPhotos = when (currentSortSelection) {
                    "A-Z" -> it.sortedBy { photo -> photo.description }
                    "Z-A" -> it.sortedByDescending { photo -> photo.description }
                    else -> it
                }

                galleryAdapter.submitList(sortedPhotos)

            }
        }

    }


    /**
     * This method draws photos onto the UI
     * It sets up the RecyclerView and the adapter with delete function
     */
    private fun drawPhotos() {
        val gallery: RecyclerView = findViewById(R.id.galleryRecyclerView)
        gallery.layoutManager = GridLayoutManager(this, 2);

        galleryAdapter = GalleryAdapter { photo ->
            photoViewModel.delete(photo)
        }

        gallery.adapter = galleryAdapter

    }

    /**
     * This method populates the spinner with sorting options and handles sorting
     */
    private fun populateSpinner() {
        val spinner: Spinner = findViewById(R.id.sortSpinner)
        val placeholder = "Select sorting"
        val values = arrayOf(placeholder, "A-Z", "Z-A")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, values)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // sort when spinner item is selected
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                currentSortSelection = parent?.getItemAtPosition(position).toString()
                handleSorting()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    /**
     * This method handles sorting of photos
     */
    private fun handleSorting() {

        // observe photos and update sorted list in adapter
        photoViewModel.allPhotos.value?.let { photos ->
            val sortedPhotos = when (currentSortSelection) {
                "A-Z" -> photos.sortedBy { photo -> photo.description }
                "Z-A" -> photos.sortedByDescending { photo -> photo.description }
                else -> photos
            }
            galleryAdapter.submitList(sortedPhotos)

        }
    }

    /**
     * This method sets up the Floating Action Button to add new photos
     * When clicked, it opens the PhotoSelectorActivity
     */
    private fun setUpFAB() {
        val fab: View = findViewById(R.id.addPhotoButton)
        fab.setOnClickListener {
            val getPhotoIntent = Intent(this, PhotoSelectorActivity::class.java)
            startActivity(getPhotoIntent)
        }
    }


}