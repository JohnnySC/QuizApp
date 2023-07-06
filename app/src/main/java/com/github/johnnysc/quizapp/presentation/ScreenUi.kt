package com.github.johnnysc.quizapp.presentation

import com.github.johnnysc.quizapp.data.ChoicesMapper
import com.github.johnnysc.quizapp.data.Question
import com.github.johnnysc.quizapp.views.ChoiceButton
import com.github.johnnysc.quizapp.views.ChoiceButtonActions
import com.github.johnnysc.quizapp.views.NextButton
import com.github.johnnysc.quizapp.views.NextButtonActions
import com.github.johnnysc.quizapp.views.QuestionTextViewAction

/**
 * @author Asatryan on 31.07.2022
 */
interface ScreenUi {

    fun next(): ScreenUi

    fun updateState(state: State)

    fun apply(
        buttons: List<ChoiceButtonActions>,
        textView: QuestionTextViewAction,
        nextButton: NextButtonActions
    )

    abstract class Abstract(private val question: Question, mapper: ChoicesMapper) : ScreenUi {

        private var state: State = State.Initial(question, mapper)

        protected abstract val nextButtonState: NextButton.State

        override fun updateState(state: State) {
            this.state = state
        }

        override fun apply(
            buttons: List<ChoiceButtonActions>,
            textView: QuestionTextViewAction,
            nextButton: NextButtonActions
        ) {
            state.apply(buttons, nextButton, nextButtonState)
            textView.show(question.question)
        }
    }

    class New(question: Question, private val nextScreenUi: ScreenUi, mapper: ChoicesMapper) :
        Abstract(question, mapper) {
        override val nextButtonState = NextButton.State.Next()
        override fun next(): ScreenUi = nextScreenUi
    }

    class Last(question: Question, mapper: ChoicesMapper) : Abstract(question, mapper) {
        override val nextButtonState = NextButton.State.NoMoreSteps()
        override fun next(): ScreenUi = throw IllegalStateException("no more steps")
    }

    class Empty(question: Question, mapper: ChoicesMapper) : Abstract(question, mapper) {
        override val nextButtonState = NextButton.State.Initial()
        override fun next(): ScreenUi = throw IllegalStateException()
    }

    interface State {

        fun apply(
            buttons: List<ChoiceButtonActions>,
            nextButton: NextButtonActions,
            nextButtonState: NextButton.State
        )

        class Initial(
            private val question: Question,
            private val mapper: ChoicesMapper
        ) : State {
            override fun apply(
                buttons: List<ChoiceButtonActions>,
                nextButton: NextButtonActions,
                nextButtonState: NextButton.State
            ) {
                nextButton.updateState(NextButton.State.Initial())

                val data: List<Pair<ChoiceButton.State, String>> = mapper.map(question)
                buttons.forEachIndexed { i, item -> item.init(data[i].first, data[i].second) }
            }
        }

        object ChoiceMade : State {
            override fun apply(
                buttons: List<ChoiceButtonActions>,
                nextButton: NextButtonActions,
                nextButtonState: NextButton.State
            ) {
                buttons.forEach { it.clear() }
                nextButton.updateState(nextButtonState)
            }
        }
    }
}