package com.example.oblig1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged

class PhotoSelectorActivity: AppCompatActivity() {
    private var uri: Uri? = null
    private lateinit var description: String

    private val openDocument = registerForActivityResult(ActivityResultContracts.OpenDocument()) { result: Uri? ->
        result?.let{
            uri = it
            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            drawImage()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_selector)

        setUpDescription()
        setUpSelect()
        setUpSave()

    }

    private fun setUpDescription(){
        val selectPhotoDescription = findViewById<EditText>(R.id.selectPhotoDescription)

        selectPhotoDescription.doOnTextChanged{ text, _, _, _ ->
            description = text.toString()
        }
    }

    private fun setUpSelect(){
        val selectPhotoButton = findViewById<Button>(R.id.selectPhotoButton)

        selectPhotoButton.setOnClickListener{
            openDocument.launch(arrayOf("image/*"))
        }
    }

    private fun setUpSave(){
        val saveButton = findViewById<Button>(R.id.selectPhotoSave)

        saveButton.setOnClickListener{
            if(uri == null || !this::description.isInitialized || description == ""){
                showDialog(getString(R.string.incorrect_select_photo_title), getString(R.string.incorrect_select_photo_desc))
            }
            uri?.let{
                val newPhoto = PhotoDescription(it, description)
                setResult(RESULT_OK, Intent().putExtra("PHOTO", newPhoto))
                finish()
            }
        }
    }

    private fun drawImage(){
        val image = findViewById<ImageView>(R.id.selectPhotoPreview)
        image.setImageURI(uri)
    }

    private fun showDialog(title:String, message: String){

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(message)

        alertDialogBuilder.setPositiveButton("OK") { _, _ ->
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }
}