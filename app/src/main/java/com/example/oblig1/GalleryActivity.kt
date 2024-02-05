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


class GalleryActivity : AppCompatActivity() {
    private var photos: ArrayList<PhotoDescription>? = ArrayList()
    private lateinit var galleryAdapter: GalleryAdapter
    private lateinit  var currentSortSelection: String

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val photo:PhotoDescription? = result.data?.getParcelableExtra("PHOTO")
                photo?.let {
                    photos?.add(it)
                    setResult(RESULT_OK, Intent().putParcelableArrayListExtra("PHOTOS", photos))

                    handleSorting()
                    galleryAdapter.notifyDataSetChanged()
                }

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        photos = intent.getParcelableArrayListExtra("PHOTOS")

        drawPhotos()
        populateSpinner()
        setUpFAB()

    }


    private fun drawPhotos(){
        val gallery: GridView = findViewById(R.id.galleryGridView)
        galleryAdapter = GalleryAdapter(this, photos ?: ArrayList())
        gallery.adapter = galleryAdapter

        gallery.setOnItemClickListener{ _, _, position, _ ->
            showConfirmationDialog(position)
            setResult(RESULT_OK, Intent().putParcelableArrayListExtra("PHOTOS", photos))
        }
    }

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

    private fun handleSorting(){
        when(currentSortSelection){
            "A-Z" -> photos?.sortBy { it.description }
            "Z-A" -> photos?.sortByDescending { it.description }
        }
        galleryAdapter.notifyDataSetChanged()
    }

    private fun setUpFAB(){
        val fab: View = findViewById(R.id.addPhotoButton)
        fab.setOnClickListener {
            val getPhotoIntent = Intent(this, PhotoSelectorActivity::class.java)
            startForResult.launch(getPhotoIntent)
        }
    }

    private fun showConfirmationDialog(position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.delete_photo_title)
        alertDialogBuilder.setMessage(R.string.delete_photo_message)

        alertDialogBuilder.setPositiveButton(R.string.yes) { _, _ ->
            photos?.removeAt(position)
            galleryAdapter.notifyDataSetChanged()
        }

        alertDialogBuilder.setNegativeButton(R.string.no) { _, _ ->
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}