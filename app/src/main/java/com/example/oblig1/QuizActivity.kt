package com.example.oblig1

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


/**
 * This class represents Quiz activity, it handles the game logic
 */
class QuizActivity : AppCompatActivity() {
    // view model for photos
    private val photoViewModel: PhotoViewModel by viewModels {
        PhotoViewModelFactory((application as PhotosApplication).repository)
    }

    // image view to show the photo
    private lateinit var imageView: ImageView

    // current game set of photos
    private var remainingPhotos: MutableList<PhotoDescription> = mutableListOf()

    // list of descriptions to choose buttons from
    private var descriptions: List<String> = listOf()

    // last toast object
    private var lastToast: Toast? = null

    // current right answer
    private lateinit var rightAnswer: String

    // current score
    private var score = 0

    // total number of attempts
    private var total = 0

    // current photo
    private var currentPhoto: PhotoDescription? = null

    // button descriptions
    private var buttonDescriptions: Array<String> = arrayOf()

    // flag to check if the game is resumed from rotating the screen
    private var resumed = false


    /**
     * Get the right answer for testing purposes
     * @return String right answer
     */
    internal fun getRightAnswer(): String {
        return rightAnswer
    }

    /**
     * Get the current score for testing purposes
     * @return Array<String> button descriptions
     */
    internal fun buttonDescriptions(): Array<String> {
        return buttonDescriptions
    }

    /**
     * This method is called when the activity is created
     * @param savedInstanceState Bundle saved state of the activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        imageView = findViewById(R.id.quizPhoto)


        photoViewModel.allPhotos.observe(this) { photos ->
            // if no photos passed or there is less than 3
            if (photos.size < 3) {
                // show warning dialog
                showEmptyItemsDialog()
            } else {
                // create deep copy of photos
                remainingPhotos = photos.toMutableList()
                getSavedData()

                if (currentPhoto != null && buttonDescriptions.isNotEmpty())
                    resumed = true

                getDescriptions()
                setButtonHandlers()
            }
        }

    }

    /**
     * Get saved data from view model
     */
    private fun getSavedData() {
        score = photoViewModel.score
        total = photoViewModel.totalScore
        currentPhoto = photoViewModel.currentPhoto
        buttonDescriptions = photoViewModel.buttonDescriptions
    }

    /**
     * Show dialog if there are not enough photos, then close the activity
     */
    private fun showEmptyItemsDialog() {

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.empty_list_title)
        alertDialogBuilder.setMessage(R.string.empty_list_description)

        alertDialogBuilder.setPositiveButton("OK") { _, _ ->
            finish()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }

    /**
     * Update score on UI and view model
     */
    private fun updateScore() {
        val scoreView = findViewById<TextView>(R.id.scoreNumber)
        scoreView.text = "$score / $total"

        photoViewModel.score = score
        photoViewModel.totalScore = total
    }

    /**
     * Get descriptions from view model, start the game
     */
    private fun getDescriptions() {
        photoViewModel.allDescriptions.observe(this) { descriptions ->
            this.descriptions = descriptions
            nextPhoto()
        }

    }

    /**
     * Main game logic loop, update score, get new photo, set up buttons
     */
    private fun nextPhoto() {
        // update score on each turn
        updateScore()

        // if the screen wasn't rotated, get a new photo
        if (!resumed) {
            // get random photo or null if no photos left
            currentPhoto = remainingPhotos.randomOrNull()

            // no photos left, create deep copy of all photos and get a new random photo
            if (currentPhoto == null) {
                remainingPhotos = photoViewModel.allPhotos.value?.toMutableList() ?: mutableListOf()
                currentPhoto = remainingPhotos.randomOrNull()
            }

            photoViewModel.currentPhoto = currentPhoto
        }

        // set the right answer to this photo's description
        rightAnswer = currentPhoto!!.description
        // remove current photo so it cannot be repeated
        remainingPhotos.remove(currentPhoto)
        drawImage(currentPhoto!!.photo)
        setUpButtonsText()

    }

    /**
     * Draw image on the screen
     */
    private fun drawImage(source: Uri) {
        imageView.setImageURI(source)
    }

    /**
     * Set up buttons descriptions
     */
    private fun setUpButtonsText() {

        // if the screen wasn't rotated, get new descriptions
        if (!resumed) {
            // get random description
            var desc2: String = descriptions.random()
            // get random description
            var desc3: String = descriptions.random()

            // if desc2 or desc3 are the same as the right answer or they are equal to another, get different ones
            while (desc2 == rightAnswer || desc3 == rightAnswer || desc2 == desc3) {
                desc2 = descriptions.random()
                desc3 = descriptions.random()
            }

            buttonDescriptions = arrayOf(rightAnswer, desc2, desc3)
            // shuffle texts so the order is random
            buttonDescriptions.shuffle()

            photoViewModel.buttonDescriptions = buttonDescriptions

        }

        // reset rotation flag
        resumed = false

        val buttonA = findViewById<Button>(R.id.answerA)
        val buttonB = findViewById<Button>(R.id.answerB)
        val buttonC = findViewById<Button>(R.id.answerC)

        buttonA.text = buttonDescriptions[0]
        buttonB.text = buttonDescriptions[1]
        buttonC.text = buttonDescriptions[2]

    }

    /**
     * This method sets up button handlers to verify the answer
     */
    private fun setButtonHandlers() {
        findViewById<Button>(R.id.answerA).setOnClickListener {
            verifyAnswer((it as Button).text.toString())
        }
        findViewById<Button>(R.id.answerB).setOnClickListener {
            verifyAnswer((it as Button).text.toString())
        }
        findViewById<Button>(R.id.answerC).setOnClickListener {
            verifyAnswer((it as Button).text.toString())
        }
    }

    /**
     * This method verifies the answer and shows the toast
     * @param text String text of the button clicked
     */
    private fun verifyAnswer(text: String) {
        // cancel last toast if any, to show the new one and prevent queueing
        lastToast?.cancel()

        // text to show in the toast
        val toastText =
            if (text == rightAnswer) getString(R.string.correct) else getString(R.string.incorrect) + " " + rightAnswer
        val toast = Toast.makeText(this, toastText, Toast.LENGTH_SHORT)
        toast.show()

        // if correct, increase the score
        if (text == rightAnswer)
            score++

        // increase the total number of attempts
        total++

        lastToast = toast
        // continue the game
        nextPhoto()

    }

    /**
     * This method is called when the activity is stopped, it shows the score in a toast
     */
    override fun onStop() {

        // if the screen is not being rotated => is finishing, show the toast
        if (!isChangingConfigurations) {
            // cancel last toast if any, to show the new one and prevent queueing
            lastToast?.cancel()

            // if not enough photos, do not show the toast
            if ((photoViewModel.allPhotos.value?.size ?: 0) < 3 || total == 0) {
                super.onStop()
                return
            }

            val toast = Toast.makeText(this, "Your score is $score / $total", Toast.LENGTH_LONG)
            toast.show()
        }

        super.onStop()
    }

}