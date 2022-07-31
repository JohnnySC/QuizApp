package com.github.johnnysc.quizapp

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorRes

/**
 * @author Asatryan on 31.07.2022
 */
class ChoiceButton : androidx.appcompat.widget.AppCompatButton {

    private var state: State = State.Initial

    //region constructors
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    //endregion

    fun init(newState: State, data: String) {
        State.Initial.show(this)
        state = newState
        text = data
    }

    fun clear() {
        state = State.Empty()
    }

    fun init(callback: ChoiceButtonClickCallback) = setOnClickListener {
        state.show(this)
        state.handle(callback)
    }

    sealed class State(@ColorRes private val colorId: Int) {

        open fun show(choiceButton: ChoiceButton) =
            choiceButton.setBackgroundColor(choiceButton.resources.getColor(colorId, null))

        open fun handle(callback: ChoiceButtonClickCallback) = Unit

        class Empty : State(0) {
            override fun show(choiceButton: ChoiceButton) = Unit
        }

        object Initial : State(R.color.teal_200)

        class Correct : State(R.color.teal_700) {
            override fun handle(callback: ChoiceButtonClickCallback) = callback.correctClicked()
        }

        class Incorrect : State(R.color.purple_200) {
            override fun handle(callback: ChoiceButtonClickCallback) = callback.incorrectClicked()
        }
    }
}

interface ChoiceButtonClickCallback {

    fun correctClicked()

    fun incorrectClicked()
}