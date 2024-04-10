package com.example.oblig1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged

/**
 * This class represents activity for adding a new photo and its description
 */
class PhotoSelectorActivity : AppCompatActivity() {

    // view model for photos
    private val photoViewModel: PhotoViewModel by viewModels {
        PhotoViewModelFactory((application as PhotosApplication).repository)
    }

    // uri of the photo
    private var uri: Uri? = null

    // description of the photo
    private lateinit var description: String

    // modern replacement of starting activity with Intent
    private val openDocument =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { result: Uri? ->
            // if any uri came back
            result?.let {
                // set the uri
                uri = it
                // if the uri is not from android resources, get permission to access it (for tests to work)
                if (it.scheme != "android.resource") {
                    // get permission to access the photo from different activities
                    contentResolver.takePersistableUriPermission(
                        it, Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
                // draw image preview
                drawImage()
            }
        }

    /**
     * This method is called when the activity is created
     * @param savedInstanceState Bundle saved state of the activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_selector)

        // set up description input
        setUpDescription()
        // set up select button
        setUpSelect()
        // set up save button
        setUpSave()

    }


    /**
     * This method sets up description field and saves the input
     */
    private fun setUpDescription() {
        val selectPhotoDescription = findViewById<EditText>(R.id.selectPhotoDescription)

        selectPhotoDescription.doOnTextChanged { text, _, _, _ ->
            description = text.toString()
        }
    }

    /**
     * This method sets up select button to open the gallery
     */
    private fun setUpSelect() {
        val selectPhotoButton = findViewById<Button>(R.id.selectPhotoButton)

        selectPhotoButton.setOnClickListener {
            openDocument.launch(arrayOf("image/*"))
        }
    }

    /**
     * This method sets up save button to save the photo and description to Room database
     */
    private fun setUpSave() {
        val saveButton = findViewById<Button>(R.id.selectPhotoSave)

        saveButton.setOnClickListener {
            // if no image is selected, no description is provided or is empty string show dialog
            if (uri == null || !this::description.isInitialized || description == "") {
                showDialog(
                    getString(R.string.incorrect_select_photo_title),
                    getString(R.string.incorrect_select_photo_desc)
                )
            } else {
                val newPhoto = PhotoDescription(photo = uri!!, description = description)
                photoViewModel.insert(newPhoto)
                setResult(RESULT_OK, Intent())

                // close activity
                finish()
            }

        }
    }

    /**
     * This method draws the image preview
     */
    private fun drawImage() {
        val image = findViewById<ImageView>(R.id.selectPhotoPreview)
        image.setImageURI(uri)
    }

    /**
     * This method shows a dialog with given title and message
     * @param title String title of the dialog
     * @param message String message of the dialog
     */
    private fun showDialog(title: String, message: String) {

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(message)

        // only needs OK button to close the window
        alertDialogBuilder.setPositiveButton("OK") { _, _ ->
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }
}