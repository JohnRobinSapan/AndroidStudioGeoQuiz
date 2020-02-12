package com.example.geoquiz

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var geoImageView: ImageView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    private var toaster: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        questionTextView = findViewById(R.id.question_text_view)
        geoImageView = findViewById(R.id.geoImageView)

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }

        questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)

        // Log a message at DEBUG log level
        Log.d(TAG, "Current question index: $quizViewModel.currentIndex")

        try {
            quizViewModel.questionBank[quizViewModel.currentIndex]
        } catch (ex: ArrayIndexOutOfBoundsException) {
            // Log a message at ERROR log level, along with an exception stack trace
            Log.e(TAG, "Index was out of bounds", ex)
        }


        val drawResource = when (quizViewModel.currentIndex) {
            0 ->
                R.drawable.australia
            1 ->
                R.drawable.ocean
            2 ->
                R.drawable.mediterranean
            3 ->
                R.drawable.egypt
            4 ->
                R.drawable.amazon
            else ->
                R.drawable.lakebaikal
        }

        geoImageView.setImageResource(drawResource)


        if (quizViewModel.questionBank[quizViewModel.currentIndex].answered) {
            setButton(false)
        } else {
            setButton(true)
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        quizViewModel.questionBank[quizViewModel.currentIndex].answered = true
        quizViewModel.answeredQuestions++
        val messageResId = if (userAnswer == correctAnswer) {
            quizViewModel.correctAnswers++
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        showToast(messageResId)
        setButton(false)

        if (quizViewModel.answeredQuestions >= quizViewModel.questionBank.size) {
            val text: String = getString(
                R.string.quiz_results,
                (quizViewModel.correctAnswers.toFloat() / quizViewModel.questionBank.size.toFloat() * 100),
                quizViewModel.correctAnswers, quizViewModel.questionBank.size
            )
            showToast(text)
        }
    }

    private fun setButton(enabled: Boolean) {
        trueButton.isEnabled = enabled
        falseButton.isEnabled = enabled
    }

    private fun showToast(text: String) {
        if (toaster != null) {
            toaster?.cancel()
        }
        toaster = Toast.makeText(
            this,
            text,
            Toast.LENGTH_LONG
        )
        toaster?.setGravity(Gravity.TOP, 0, 200)
        toaster?.show()
    }

    private fun showToast(text: Int) {
        if (toaster != null) {
            toaster?.cancel()
        }
        toaster = Toast.makeText(
            this,
            text,
            Toast.LENGTH_SHORT
        )
        toaster?.setGravity(Gravity.TOP, 0, 200)
        toaster?.show()
    }
}
