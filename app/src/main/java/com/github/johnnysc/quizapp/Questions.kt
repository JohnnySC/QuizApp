package com.github.johnnysc.quizapp

import com.google.gson.annotations.SerializedName

/**
 * @author Asatryan on 31.07.2022
 */
data class Questions(
    @SerializedName("questions")
    val questions: List<Question>
)

data class Question(
    @SerializedName("question")
    val question: String,
    @SerializedName("correct")
    val correct: String,
    @SerializedName("incorrects")
    val incorrects: List<String>
)