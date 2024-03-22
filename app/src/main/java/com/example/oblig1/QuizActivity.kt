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
    private val photoViewModel: PhotoViewModel by viewModels {
        PhotoViewModelFactory((application as PhotosApplication).repository)
    }

    private lateinit var imageView: ImageView

    // current game set of photos
    private var remainingPhotos: MutableList<PhotoDescription> = mutableListOf()
    // list of descriptions to choose buttons from
    private var descriptions: List<String> = listOf()
    // last toast object
    private var lastToast: Toast? = null
    // current right answer

    private lateinit var rightAnswer: String

    private var score = 0
    private var total = 0
    private var currentPhoto: PhotoDescription? = null
    private var buttonDescriptions: Array<String> = arrayOf()
    private var resumed = false


    internal fun getRightAnswer(): String {
        return rightAnswer
    }

    internal fun buttonDescriptions(): Array<String> {
        return buttonDescriptions
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        imageView = findViewById(R.id.quizPhoto)

        // if no photos passed or there is less than 3

        photoViewModel.allPhotos.observe(this) { photos ->
            if (photos.size < 3) {
                // show warning dialog
                showEmptyItemsDialog()
            } else {
                // create deep copy of photos
                remainingPhotos = photos.toMutableList()
                getSavedData()

                if(currentPhoto != null && buttonDescriptions.isNotEmpty())
                    resumed = true

                getDescriptions()
                setButtonHandlers()
            }
        }

    }

    private fun getSavedData(){
        score = photoViewModel.score
        total = photoViewModel.totalScore
        currentPhoto = photoViewModel.currentPhoto
        buttonDescriptions = photoViewModel.buttonDescriptions
    }

    // method to show warning dialog about not enough items, finishes activity
    private fun showEmptyItemsDialog(){

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle(R.string.empty_list_title)
        alertDialogBuilder.setMessage(R.string.empty_list_description)

        alertDialogBuilder.setPositiveButton("OK") { _, _ ->
            finish()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

    }

    // update score in UI
    private fun updateScore(){
        val scoreView = findViewById<TextView>(R.id.scoreNumber)
        scoreView.text = "$score / $total"

        photoViewModel.score = score
        photoViewModel.totalScore = total
    }

    // get descriptions from db
    private fun getDescriptions(){
        photoViewModel.allDescriptions.observe(this) { descriptions ->
            this.descriptions = descriptions
            nextPhoto()
        }

    }

    // main game logic
    private fun nextPhoto(){
        // update score on each turn
        updateScore()

        if(!resumed){
            // get random photo or null if no photos left
            currentPhoto = remainingPhotos.randomOrNull()

            // no photos left, create deep copy of all photos and get a new random photo
            if(currentPhoto == null){
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

    // method to draw image onto UI
    private fun drawImage(source: Uri){
        imageView.setImageURI(source)
    }

    // set up texts on buttons
    private fun setUpButtonsText(){

        if(!resumed){
            // get random description
            var desc2: String = descriptions.random()
            // get random description
            var desc3: String = descriptions.random()

            // if desc2 or desc3 are the same as the right answer or they are equal to another, get different ones
            while (desc2 == rightAnswer || desc3 == rightAnswer || desc2 == desc3){
                desc2 = descriptions.random()
                desc3 = descriptions.random()
            }

            buttonDescriptions = arrayOf(rightAnswer, desc2, desc3)
            // shuffle texts so the order is random
            buttonDescriptions.shuffle()

            photoViewModel.buttonDescriptions = buttonDescriptions

        }

        resumed = false

        val buttonA = findViewById<Button>(R.id.answerA)
        val buttonB = findViewById<Button>(R.id.answerB)
        val buttonC = findViewById<Button>(R.id.answerC)

        buttonA.text = buttonDescriptions[0]
        buttonB.text = buttonDescriptions[1]
        buttonC.text = buttonDescriptions[2]

    }

    // method to set up listeners on all buttons
    private fun setButtonHandlers(){
        findViewById<Button>(R.id.answerA).setOnClickListener{
            verifyAnswer((it as Button).text.toString() )
        }
        findViewById<Button>(R.id.answerB).setOnClickListener{
            verifyAnswer((it as Button).text.toString() )
        }
        findViewById<Button>(R.id.answerC).setOnClickListener{
            verifyAnswer((it as Button).text.toString() )
        }
    }

    // verify is the clicked button is correct
    private fun verifyAnswer(text: String){
        // cancel last toast if any, to show the new one and prevent queueing
        lastToast?.cancel()

        // text to show in the toast
        val toastText = if(text == rightAnswer) getString(R.string.correct) else getString(R.string.incorrect) + " " + rightAnswer
        val toast = Toast.makeText(this, toastText, Toast.LENGTH_SHORT)
        toast.show()

        // if correct, increase the score
        if(text == rightAnswer)
            score++

        // increase the total number of attempts
        total++

        lastToast = toast
        // continue the game
        nextPhoto()

    }

    // on close show toast with score
    override fun onStop() {

        if (!isChangingConfigurations){
            // cancel last toast if any, to show the new one and prevent queueing
            lastToast?.cancel()

            // if not enough photos, do not show the toast
            if((photoViewModel.allPhotos.value?.size ?: 0) < 3 || total == 0){
                super.onStop()
                return
            }


            val toast = Toast.makeText(this, "Your score is $score / $total", Toast.LENGTH_LONG)
            toast.show()
        }

        super.onStop()
    }

}