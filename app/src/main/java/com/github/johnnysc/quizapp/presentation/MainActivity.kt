package com.github.johnnysc.quizapp.presentation

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.johnnysc.quizapp.ProvideViewModel
import com.github.johnnysc.quizapp.R
import com.github.johnnysc.quizapp.views.ChoiceButton
import com.github.johnnysc.quizapp.views.NextButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = (application as ProvideViewModel).provideViewModel()

        val nextButton = findViewById<NextButton>(R.id.nextButton)
        val buttonIds = listOf(R.id.buttonOne, R.id.buttonTwo, R.id.buttonThree, R.id.buttonFour)
        val buttons: List<ChoiceButton> = buttonIds.map { findViewById(it) }
        val question: TextView = findViewById(R.id.questionTextView)

        for (button in buttons) { button.init(viewModel) }
        nextButton.init(viewModel)

        viewModel.observe(this) { it.apply(buttons, question, nextButton) }

        viewModel.liveData.observe(this) {
            Toast.makeText(this,it, Toast.LENGTH_SHORT).show()
        }
    }
}