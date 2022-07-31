package com.github.johnnysc.quizapp

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StringRes

/**
 * @author Asatryan on 31.07.2022
 */
class NextButton : androidx.appcompat.widget.AppCompatButton {

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

    fun init(nextAction: NextAction) = setOnClickListener {
        state.handle(nextAction)
    }

    fun updateState(newState: State) {
        state = newState
        state.show(this)
    }

    sealed class State(private val visibility: Int) {

        open fun show(nextButton: NextButton) {
            nextButton.visibility = visibility
        }

        open fun handle(nextAction: NextAction) = Unit

        class Initial : State(View.INVISIBLE)

        abstract class Available(@StringRes private val stringId: Int) : State(View.VISIBLE) {

            override fun show(nextButton: NextButton) {
                super.show(nextButton)
                nextButton.setText(stringId)
            }
        }

        class Next : Available(R.string.next) {
            override fun handle(nextAction: NextAction) = nextAction.goToNextScreen()
        }

        class NoMoreSteps : Available(R.string.finish) {
            override fun handle(nextAction: NextAction) = nextAction.finish()
        }
    }
}

interface NextAction {

    fun goToNextScreen()

    fun finish()
}