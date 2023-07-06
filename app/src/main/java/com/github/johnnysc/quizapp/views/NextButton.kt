package com.github.johnnysc.quizapp.views

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StringRes
import com.github.johnnysc.quizapp.R
import kotlinx.parcelize.Parcelize

/**
 * @author Asatryan on 31.07.2022
 */
class NextButton : androidx.appcompat.widget.AppCompatButton, NextButtonActions {

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

    init {
        state.show(this)
    }

    override fun init(nextAction: NextAction) = setOnClickListener {
        state.handle(nextAction)
    }

    override fun updateState(newState: State) {
        state = newState
        state.show(this)
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val myState = NextButtonState(superState)
        myState.state = state
        return myState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as NextButtonState
        super.onRestoreInstanceState(savedState.superState)
        updateState(savedState.state)
    }

    interface State : Parcelable {

        fun show(nextButton: NextButton)

        fun handle(nextAction: NextAction) = Unit

        abstract class Abstract(private val visibility: Int) : State {

            override fun show(nextButton: NextButton) {
                nextButton.visibility = visibility
            }
        }

        @Parcelize
        class Initial : Abstract(View.INVISIBLE)

        abstract class Available(@StringRes private val stringId: Int) : Abstract(View.VISIBLE) {

            override fun show(nextButton: NextButton) {
                super.show(nextButton)
                nextButton.setText(stringId)
            }
        }

        @Parcelize
        class Next : Available(R.string.next) {
            override fun handle(nextAction: NextAction) = nextAction.goToNextScreen()
        }

        @Parcelize
        class NoMoreSteps : Available(R.string.finish) {
            override fun handle(nextAction: NextAction) = nextAction.finish()
        }
    }
}

interface NextAction {

    fun goToNextScreen()

    fun finish()
}

interface NextButtonActions {

    fun init(nextAction: NextAction)

    fun updateState(newState: NextButton.State)
}