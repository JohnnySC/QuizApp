package com.github.johnnysc.quizapp

import android.widget.TextView

/**
 * @author Asatryan on 31.07.2022
 */
abstract class ScreenUi(private val question: Question, mapper: ChoicesMapper) {

    private var state: State = State.Initial(question, mapper)

    protected abstract val nextButtonState: NextButton.State

    abstract fun next(): ScreenUi

    fun updateState(state: State) {
        this.state = state
    }

    fun apply(
        buttons: List<ChoiceButton>,
        textView: TextView,
        nextButton: NextButton
    ) {
        state.apply(buttons, nextButton, nextButtonState)
        textView.text = question.question
    }

    class New(question: Question, private val nextScreenUi: ScreenUi, mapper: ChoicesMapper) :
        ScreenUi(question, mapper) {
        override val nextButtonState = NextButton.State.Next()
        override fun next(): ScreenUi = nextScreenUi
    }

    class Last(question: Question, mapper: ChoicesMapper) : ScreenUi(question, mapper) {
        override val nextButtonState = NextButton.State.NoMoreSteps()
        override fun next(): ScreenUi = throw IllegalStateException("no more steps")
    }

    class Empty(question: Question, mapper: ChoicesMapper) : ScreenUi(question, mapper) {
        override val nextButtonState = NextButton.State.Initial()
        override fun next(): ScreenUi = throw IllegalStateException()
    }

    sealed class State {

        abstract fun apply(
            buttons: List<ChoiceButton>,
            nextButton: NextButton, nextButtonState: NextButton.State
        )

        class Initial(
            private val question: Question,
            private val mapper: ChoicesMapper
        ) : State() {
            override fun apply(
                buttons: List<ChoiceButton>,
                nextButton: NextButton,
                nextButtonState: NextButton.State
            ) {
                nextButton.updateState(NextButton.State.Initial())

                val data: List<Pair<ChoiceButton.State, String>> = mapper.map(question)
                buttons.forEachIndexed { i, item -> item.init(data[i].first, data[i].second) }
            }
        }

        object ChoiceMade : State() {
            override fun apply(
                buttons: List<ChoiceButton>,
                nextButton: NextButton,
                nextButtonState: NextButton.State
            ) {
                buttons.forEach { it.clear() }
                nextButton.updateState(nextButtonState)
            }
        }
    }
}