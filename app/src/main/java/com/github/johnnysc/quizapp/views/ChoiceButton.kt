package com.github.johnnysc.quizapp.views

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import androidx.annotation.ColorRes
import com.github.johnnysc.quizapp.R
import kotlinx.parcelize.Parcelize

/**
 * @author Asatryan on 31.07.2022
 */
class ChoiceButton : androidx.appcompat.widget.AppCompatButton, ChoiceButtonActions {

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

    override fun init(newState: State, data: String) {
        currentState = State.Initial()
        currentState.show(this)
        state = newState
        text = data
    }

    override fun clear() {
        state = State.Empty()
    }

    override fun init(callback: ChoiceButtonClickCallback) = setOnClickListener {
        currentState = state
        state.show(this)
        state.handle(callback)
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val myState =
            ChoiceButtonState(superState)
        myState.state = state
        myState.currentState = currentState
        myState.name = text.toString()
        return myState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as ChoiceButtonState
        super.onRestoreInstanceState(savedState.superState)
        text = savedState.name
        this.state = savedState.state
        currentState = savedState.currentState
        currentState.show(this)
    }

    interface State : Parcelable {

        fun show(choiceButton: ChoiceButton)

        fun handle(callback: ChoiceButtonClickCallback) = Unit

        abstract class Abstract(@ColorRes private val colorId: Int) : State {

            override fun show(choiceButton: ChoiceButton) = with(choiceButton) {
                setBackgroundColor(resources.getColor(colorId, null))
            }
        }

        @Parcelize
        class Empty : Abstract(R.color.teal_200) {
            override fun show(choiceButton: ChoiceButton) = Unit
        }

        @Parcelize
        class Initial : Abstract(R.color.teal_200)

        @Parcelize
        class Correct : Abstract(R.color.teal_700) {
            override fun handle(callback: ChoiceButtonClickCallback) = callback.correctClicked()
        }

        @Parcelize
        class Incorrect : Abstract(R.color.purple_200) {
            override fun handle(callback: ChoiceButtonClickCallback) = callback.incorrectClicked()
        }
    }
}

interface ChoiceButtonClickCallback {

    fun correctClicked()

    fun incorrectClicked()
}

interface ChoiceButtonActions {

    fun init(newState: ChoiceButton.State, data: String)

    fun clear()

    fun init(callback: ChoiceButtonClickCallback)
}