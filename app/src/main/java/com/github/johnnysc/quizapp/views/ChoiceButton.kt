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

    //region constructors
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    //endregion

    override fun getFreezesText() = true

    private var nextState: State = State.Initial()
    private var currentState: State = State.Initial()

    override fun init(newState: State, data: String) {
        currentState = State.Initial()
        currentState.map(Show(this))
        nextState = newState
        text = data
    }

    override fun clear() {
        nextState = currentState.map(MakeEmptyState)
    }

    override fun init(callback: ChoiceButtonClickCallback) = setOnClickListener {
        currentState = nextState
        currentState.map(Show(this))
        currentState.handle(callback)
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val myState = ChoiceButtonState(superState)
        myState.nextState = nextState
        myState.currentState = currentState
        return myState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as ChoiceButtonState
        super.onRestoreInstanceState(savedState.superState)
        nextState = savedState.nextState
        currentState = savedState.currentState
        currentState.map(Show(this))
    }

    interface State : Parcelable {

        interface Mapper<T : Any> {
            fun map(colorId: Int): T
        }

        fun <T : Any> map(mapper: Mapper<T>): T

        fun handle(callback: ChoiceButtonClickCallback) = Unit

        abstract class Abstract(@ColorRes private val colorId: Int) : State {

            override fun <T : Any> map(mapper: Mapper<T>): T = mapper.map(colorId)
        }

        @Parcelize
        class Empty(private val colorId: Int) : Abstract(colorId)

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

object MakeEmptyState : ChoiceButton.State.Mapper<ChoiceButton.State> {
    override fun map(colorId: Int) = ChoiceButton.State.Empty(colorId)
}

class Show(private val choiceButton: ChoiceButton) : ChoiceButton.State.Mapper<Unit> {
    override fun map(colorId: Int) = with(choiceButton) {
        setBackgroundColor(resources.getColor(colorId, null))
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