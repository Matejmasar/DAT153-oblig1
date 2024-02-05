package com.example.oblig1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class QuizActivity : AppCompatActivity() {
    private var score: Int = 0
    private var total: Int = 0
    private var allPhotos: ArrayList<PhotoDescription>? = ArrayList()
    private var remainingPhotos: ArrayList<PhotoDescription> = ArrayList()
    private var descriptions: ArrayList<String> = ArrayList()
    private var lastToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        score = intent.getIntExtra("SCORE", 0)
        total = intent.getIntExtra("SCORE_TOTAL", 0)
        allPhotos = intent.getParcelableArrayListExtra("PHOTOS")

        if(allPhotos == null || allPhotos!!.isEmpty()){
            setResult(RESULT_OK, Intent().putExtra("SCORE", score).putExtra("SCORE_TOTAL", total))
            showEmptyItemsDialog()
        }


        remainingPhotos = ArrayList(allPhotos!!)
        Log.d("test", "On create Quiz")
        Log.d("test", "$remainingPhotos")
        Log.d("test", "$score")
        Log.d("test", "$total")
        updateScore()
        getDescriptions()
        game()
    }

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

    private fun updateScore(){
        val scoreView = findViewById<TextView>(R.id.scoreNumber)
        scoreView.text = "$score / $total"

        setResult(RESULT_OK, Intent().putExtra("SCORE", score).putExtra("SCORE_TOTAL", total))
    }

    private fun getDescriptions(){
        allPhotos?.let{
            for(photo in it){
                descriptions.add(photo.description)
            }
        }
    }


    private fun game(){
        remainingPhotos.shuffle()
        nextPhoto()
    }

    private fun nextPhoto(){
        updateScore()

        var photo = remainingPhotos.randomOrNull()

        if(photo == null){
            remainingPhotos = ArrayList(allPhotos!!)
            remainingPhotos.shuffle()
            photo = remainingPhotos.randomOrNull()
        }

        remainingPhotos.remove(photo)
        drawImage(photo!!.photo)
        setUpButtons(photo.description)

    }

    private fun drawImage(source: Any){
        val image = findViewById<ImageView>(R.id.quizPhoto)

        if(source is Int)
            image.setImageResource(source)
        else if(source is Uri)
            image.setImageURI(source)
    }

    private fun setUpButtons(trueDesc: String){

        var desc2: String = descriptions.random()
        var desc3: String = descriptions.random()

        while (desc2 == trueDesc || desc3 == trueDesc || desc2 == desc3){
            desc2 = descriptions.random()
            desc3 = descriptions.random()
        }

        val answers = arrayOf(trueDesc, desc2, desc3)
        answers.shuffle()

        val buttonA = findViewById<Button>(R.id.answerA)
        val buttonB = findViewById<Button>(R.id.answerB)
        val buttonC = findViewById<Button>(R.id.answerC)

        buttonA.text = answers[0]
        buttonB.text = answers[1]
        buttonC.text = answers[2]

        setButtonHandlers(trueDesc, buttonA, buttonB, buttonC)

    }

    private fun setButtonHandlers(trueDesc: String, buttonA: View, buttonB: View, buttonC: View){
        buttonA.setOnClickListener{
            verifyAnswer(trueDesc, (it as Button).text.toString() )
        }
        buttonB.setOnClickListener{
            verifyAnswer(trueDesc, (it as Button).text.toString() )
        }
        buttonC.setOnClickListener{
            verifyAnswer(trueDesc, (it as Button).text.toString() )
        }
    }

    private fun verifyAnswer(trueDesc: String, text: String){
        lastToast?.cancel()

        val toastText = if(text == trueDesc) R.string.correct else R.string.incorrect
        val toast = Toast.makeText(this, toastText, Toast.LENGTH_SHORT)
        toast.show()

        if(text == trueDesc)
            score++

        total++

        lastToast = toast
        nextPhoto()

    }

}