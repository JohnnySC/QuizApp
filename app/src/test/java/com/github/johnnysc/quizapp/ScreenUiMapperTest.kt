package com.github.johnnysc.quizapp

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

/**
 * @author Asatryan on 31.07.2022
 */
class ScreenUiMapperTest {

    @Test
    fun test() {
        val mapper = ScreenUiMapper.Base()

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