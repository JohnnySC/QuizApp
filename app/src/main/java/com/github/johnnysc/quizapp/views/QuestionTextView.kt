package com.github.johnnysc.quizapp.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class QuestionTextView : AppCompatTextView, QuestionTextViewAction {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun show(text: String) {
        this.text = text
    }
}

interface QuestionTextViewAction {

    fun show(text: String)
}