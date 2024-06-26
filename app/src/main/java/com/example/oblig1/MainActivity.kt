package com.example.oblig1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

/**
 *  This class represents the Main activity and is launched when opening the app
 */
class MainActivity : AppCompatActivity() {

    /**
     * This method is called when the activity is created
     * @param savedInstanceState Bundle saved state of the activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpGallery()
        setUpQuiz()
    }

    /**
     * This method sets up listener on Gallery button
     * When clicked, it opens the GalleryActivity
     */
    private fun setUpGallery() {
        val galleryButton = findViewById<Button>(R.id.gallery_button)

        galleryButton.setOnClickListener {
            val galleryIntent = Intent(this, GalleryActivity::class.java)
            startActivity(galleryIntent)

        }
    }

    /**
     * This method sets up listener on Quiz button
     * When clicked, it opens the QuizActivity
     */
    private fun setUpQuiz() {
        val quizButton = findViewById<Button>(R.id.quiz_button)

        quizButton.setOnClickListener {
            val quizIntent = Intent(this, QuizActivity::class.java)
            startActivity(quizIntent)
        }
    }
}