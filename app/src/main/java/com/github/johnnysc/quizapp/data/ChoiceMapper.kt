package com.github.johnnysc.quizapp.data

import com.github.johnnysc.quizapp.presentation.Mapper
import com.github.johnnysc.quizapp.views.ChoiceButton

/**
 * @author Asatryan on 31.07.2022
 */
interface ChoicesMapper : Mapper<List<Pair<ChoiceButton.State, String>>, Question> {

    abstract class Abstract(private val reorder: Reorder) : ChoicesMapper {

        override fun map(source: Question): List<Pair<ChoiceButton.State, String>> {
            val data: MutableList<Pair<ChoiceButton.State, String>> = ArrayList(4)
            data.add(Pair(ChoiceButton.State.Correct(), source.correct))
            data.addAll(source.incorrects.map { Pair(ChoiceButton.State.Incorrect(), it) })
            return reorder.map(data)
        }
    }

    class Base : Abstract(Reorder.Shuffle())
    class Mock : Abstract(Reorder.None())
}

interface Reorder :
    Mapper<List<Pair<ChoiceButton.State, String>>, List<Pair<ChoiceButton.State, String>>> {

    class Shuffle : Reorder {
        override fun map(source: List<Pair<ChoiceButton.State, String>>) = source.shuffled()
    }

    class None : Reorder {
        override fun map(source: List<Pair<ChoiceButton.State, String>>) = source
    }
}