package com.github.johnnysc.quizapp

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import androidx.annotation.ColorRes
import kotlinx.parcelize.Parcelize

/**
 * @author Asatryan on 31.07.2022
 */
class ChoiceButton : androidx.appcompat.widget.AppCompatButton {

    private var state: State = State.Initial()

    //region constructors
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    //endregion

    private var currentState: State = state

    fun init(newState: State, data: String) {
        currentState = State.Initial()
        currentState.show(this)
        state = newState
        text = data
    }

    fun clear() {
        state = State.Empty()
    }

    fun init(callback: ChoiceButtonClickCallback) = setOnClickListener {
        currentState = state
        state.show(this)
        state.handle(callback)
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val myState = ChoiceButtonState(superState)
        myState.state = currentState
        myState.name = text.toString()
        return myState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as ChoiceButtonState
        super.onRestoreInstanceState(savedState.superState)
        text = savedState.name
        currentState = savedState.state
        currentState.show(this)
    }

    @Parcelize
    sealed class State(@ColorRes private val colorId: Int) : Parcelable {

        open fun show(choiceButton: ChoiceButton) =
            choiceButton.setBackgroundColor(choiceButton.resources.getColor(colorId, null))

        open fun handle(callback: ChoiceButtonClickCallback) = Unit

        class Empty : State(0) {
            override fun show(choiceButton: ChoiceButton) = Unit
        }

        class Initial : State(R.color.teal_200)

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