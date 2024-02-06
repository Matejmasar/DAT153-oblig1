package com.example.oblig1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts

/**
 *  This class represents the Main activity and is launched when opening the app
 *  It handles the communication between Gallery and Quiz
 */
class MainActivity : AppCompatActivity() {

    // Default photos
    private var photos = arrayListOf(
        PhotoDescription(R.drawable.cat, "Cat"),
        PhotoDescription(R.drawable.dog, "Dog"),
        PhotoDescription(R.drawable.lion, "Lion")
    )

    // default score
    private var score = 0
    // default attempts
    private var scoreTotal = 0


    // modern replacement of startActivityForResult and onActivityResult
    private val startGalleryForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                // get current photos from gallery, new method loses support for my min API
                val returnedPhotos: ArrayList<PhotoDescription>? = data?.getParcelableArrayListExtra("PHOTOS")

                // if anything was supplied, copy it to photos
                returnedPhotos?.let{
                    photos = ArrayList(it)
                }
            }
        }

    private val startQuizForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // save the score
                score = result.data?.getIntExtra("SCORE", 0)!!
                // save the total attempts
                scoreTotal = result.data?.getIntExtra("SCORE_TOTAL", 0)!!
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpGallery()
        setUpQuiz()
    }

    // sets up listener on Gallery button
    private fun setUpGallery(){
        val galleryButton = findViewById<Button>(R.id.gallery_button)

        galleryButton.setOnClickListener{
            // send photos to gallery
            val galleryIntent = Intent(this, GalleryActivity::class.java).putParcelableArrayListExtra("PHOTOS", photos)
            startGalleryForResult.launch(galleryIntent)
        }
    }

    // sets up listener on Quiz button
    private fun setUpQuiz(){
        val quizButton = findViewById<Button>(R.id.quiz_button)

        quizButton.setOnClickListener{
            val quizIntent = Intent(this, QuizActivity::class.java)
                // send photos to display and populate buttons with text
                .putParcelableArrayListExtra("PHOTOS", photos)
                // send score
                .putExtra("SCORE", score)
                // send total attempts
                .putExtra("SCORE_TOTAL", scoreTotal)
            startQuizForResult.launch(quizIntent)
        }
    }
}

