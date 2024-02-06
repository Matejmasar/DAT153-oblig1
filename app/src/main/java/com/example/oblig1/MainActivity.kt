package com.example.oblig1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private var photos = arrayListOf(
        PhotoDescription(R.drawable.cat, "Cat"),
        PhotoDescription(R.drawable.dog, "Dog"),
        PhotoDescription(R.drawable.lion, "Lion")
    )

    private var score = 0
    private var scoreTotal = 0


    private val startGalleryForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val returnedPhotos: ArrayList<PhotoDescription>? = data?.getParcelableArrayListExtra("PHOTOS")

                returnedPhotos?.let{
                    photos = ArrayList(it)
                }
            }
        }

    private val startQuizForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                score = result.data?.getIntExtra("SCORE", 0)!!
                scoreTotal = result.data?.getIntExtra("SCORE_TOTAL", 0)!!
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpGallery()
        setUpQuiz()

    }

    private fun setUpGallery(){
        val galleryButton = findViewById<Button>(R.id.gallery_button)

        galleryButton.setOnClickListener{
            val galleryIntent = Intent(this, GalleryActivity::class.java).putParcelableArrayListExtra("PHOTOS", photos)
            startGalleryForResult.launch(galleryIntent)
        }
    }

    private fun setUpQuiz(){
        val quizButton = findViewById<Button>(R.id.quiz_button)

        quizButton.setOnClickListener{
            val quizIntent = Intent(this, QuizActivity::class.java)
                .putParcelableArrayListExtra("PHOTOS", photos)
                .putExtra("SCORE", score)
                .putExtra("SCORE_TOTAL", scoreTotal)
            startQuizForResult.launch(quizIntent)
        }
    }
}

