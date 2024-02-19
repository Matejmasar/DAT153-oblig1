package com.example.oblig1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.Spinner
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog

/**
 * This class represents the Gallery, with its photos, descriptions, button to add new photos,
 * handling deleting photos, etc.
 */
class GalleryActivity : AppCompatActivity() {
    // Data Manager
    private lateinit var dataManager: DataManager
    // adapter to populate photos into UI
    private lateinit var galleryAdapter: GalleryAdapter
    // remembering current sorting value
    private lateinit  var currentSortSelection: String

    // modern replacement of startActivityForResult and onActivityResult
    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // sort if needed
                handleSorting()
                // update UI
                galleryAdapter.notifyDataSetChanged()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        dataManager = DataManager.instance

        drawPhotos()
        populateSpinner()
        setUpFAB()

    }


    // draw photos onto UI
    private fun drawPhotos(){
        val gallery: GridView = findViewById(R.id.galleryGridView)
        galleryAdapter = GalleryAdapter(this, dataManager.getAllPhotos() ?: ArrayList())
        gallery.adapter = galleryAdapter

        // set on click listener for each item
        gallery.setOnItemClickListener{ _, _, position, _ ->
            showConfirmationDialog(position)
        }
    }

    // populate spinner with sorting options
    private fun populateSpinner(){
        val spinner: Spinner = findViewById(R.id.sortSpinner)
        val placeholder = "Select sorting"
        val values = arrayOf(placeholder, "A-Z", "Z-A")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, values)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentSortSelection = parent?.getItemAtPosition(position).toString()
                handleSorting()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    // sort photos based on criteria
    private fun handleSorting(){
        when(currentSortSelection){
            "A-Z" -> dataManager.getAllPhotos()?.sortBy { it.description }
            "Z-A" -> dataManager.getAllPhotos()?.sortByDescending { it.description }
        }
        galleryAdapter.notifyDataSetChanged()
    }

    // set up Floating Action Button
    private fun setUpFAB(){
        val fab: View = findViewById(R.id.addPhotoButton)
        fab.setOnClickListener {
            val getPhotoIntent = Intent(this, PhotoSelectorActivity::class.java)
            startForResult.launch(getPhotoIntent)
        }
    }

    // show confirmation dialog before deleting photo
    private fun showConfirmationDialog(position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.delete_photo_title)
        alertDialogBuilder.setMessage(R.string.delete_photo_message)

        alertDialogBuilder.setPositiveButton(R.string.yes) { _, _ ->
            // remove photo
            dataManager.removePhoto(position)

            // update UI
            galleryAdapter.notifyDataSetChanged()
        }

        alertDialogBuilder.setNegativeButton(R.string.no) { _, _ ->
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}