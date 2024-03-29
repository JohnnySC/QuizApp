package com.github.johnnysc.quizapp

import com.github.johnnysc.quizapp.data.ChoicesMapper
import com.github.johnnysc.quizapp.data.Question
import com.github.johnnysc.quizapp.data.Questions
import com.github.johnnysc.quizapp.presentation.ScreenUi
import com.github.johnnysc.quizapp.presentation.ScreenUiMapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

/**
 * @author Asatryan on 31.07.2022
 */
class ScreenUiMapperTest {

    @Test
    fun test() {
        val mapper = ScreenUiMapper.Base(ChoicesMapper.Mock())

        val actual = mapper.map(
            Questions(
                listOf(
                    Question("1", "A1", listOf("b", "c", "d")),
                    Question("2", "B1", listOf("e", "f", "g")),
                    Question("3", "C1", listOf("h", "i", "j")),
                )
            )
        )
        assertEquals(true, actual is ScreenUi.New)
        assertEquals(true, actual.next() is ScreenUi.New)
        assertEquals(true, actual.next().next() is ScreenUi.Last)
        assertThrows(IllegalStateException::class.java) {
            actual.next().next().next() is ScreenUi.Last
        }
    }
}