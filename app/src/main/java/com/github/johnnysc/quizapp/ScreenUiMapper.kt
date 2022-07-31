package com.github.johnnysc.quizapp

interface ScreenUiMapper : Repository.Mapper<ScreenUi> {

    class Base(private val mapper: ChoicesMapper) : ScreenUiMapper {

        override fun map(questions: Questions): ScreenUi {
            val list = questions.questions
            var screenUi: ScreenUi = ScreenUi.Empty(list.first(), mapper)
            var last: ScreenUi = ScreenUi.Last(list.last(), mapper)

            for (i in list.size - 1 downTo 1) {
                screenUi = ScreenUi.New(list[i - 1], last, mapper)
                last = screenUi
            }
            return screenUi
        }
    }
}