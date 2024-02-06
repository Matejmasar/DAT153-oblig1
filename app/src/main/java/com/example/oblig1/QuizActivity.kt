package com.example.oblig1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

/**
 * This class represents Quiz activity, it handles the game logic
 */
class QuizActivity : AppCompatActivity() {
    // reached score
    private var score: Int = 0
    // total number of attempts
    private var total: Int = 0
    // all photos to choose from
    private var allPhotos: ArrayList<PhotoDescription>? = ArrayList()
    // current game set of photos
    private var remainingPhotos: ArrayList<PhotoDescription> = ArrayList()
    // list of descriptions to choose buttons from
    private var descriptions: ArrayList<String> = ArrayList()
    // last toast object
    private var lastToast: Toast? = null
    // current right answer
    private lateinit var rightAnswer: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // get last score
        score = intent.getIntExtra("SCORE", 0)
        // get last number of total attempts
        total = intent.getIntExtra("SCORE_TOTAL", 0)
        // get all photos to choose from
        allPhotos = intent.getParcelableArrayListExtra("PHOTOS")

        // if no photos passed or there is less than 3
        if(allPhotos == null || allPhotos!!.size < 3){
            setResult(RESULT_OK, Intent().putExtra("SCORE", score).putExtra("SCORE_TOTAL", total))
            // show warning dialog
            showEmptyItemsDialog()
        }
        // else we can play
        else{
            // create deep copy of photos
            remainingPhotos = ArrayList(allPhotos!!)

            getDescriptions()
            setButtonHandlers()
            nextPhoto()
        }

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

        // update result
        setResult(RESULT_OK, Intent().putExtra("SCORE", score).putExtra("SCORE_TOTAL", total))
    }

    // loop over all photos and get their descriptions
    private fun getDescriptions(){
        allPhotos?.let{
            for(photo in it){
                descriptions.add(photo.description)
            }
        }
    }

    // main game logic
    private fun nextPhoto(){
        // update score on each turn
        updateScore()

        // get random photo or null if no photos left
        var photo = remainingPhotos.randomOrNull()

        // no photos left, create deep copy of all photos and get a new random photo
        if(photo == null){
            remainingPhotos = ArrayList(allPhotos!!)
            photo = remainingPhotos.randomOrNull()
        }

        // set the right answer to this photo's description
        rightAnswer = photo!!.description
        // remove current photo so it cannot be repeated
        remainingPhotos.remove(photo)
        drawImage(photo.photo)
        setUpButtonsText(photo.description)

    }

    // method to draw image onto UI
    private fun drawImage(source: Any){
        val image = findViewById<ImageView>(R.id.quizPhoto)

        // if source is Int => it comes from R.drawable
        if(source is Int)
            image.setImageResource(source)
        // else it comes from user's phone
        else if(source is Uri)
            image.setImageURI(source)
    }

    // set up texts on buttons
    private fun setUpButtonsText(trueDesc: String){

        // get random description
        var desc2: String = descriptions.random()
        // get random description
        var desc3: String = descriptions.random()

        // if desc2 or desc3 are the same as the right answer or they are equal to another, get different ones
        while (desc2 == trueDesc || desc3 == trueDesc || desc2 == desc3){
            desc2 = descriptions.random()
            desc3 = descriptions.random()
        }

        val answers = arrayOf(trueDesc, desc2, desc3)
        // shuffle texts so the order is random
        answers.shuffle()

        val buttonA = findViewById<Button>(R.id.answerA)
        val buttonB = findViewById<Button>(R.id.answerB)
        val buttonC = findViewById<Button>(R.id.answerC)

        buttonA.text = answers[0]
        buttonB.text = answers[1]
        buttonC.text = answers[2]

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

}