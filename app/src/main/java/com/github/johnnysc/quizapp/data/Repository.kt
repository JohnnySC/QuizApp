package com.github.johnnysc.quizapp.data

import android.content.Context
import androidx.annotation.RawRes
import com.github.johnnysc.quizapp.R
import com.google.gson.Gson

/**
 * @author Asatryan on 31.07.2022
 */
interface Repository {

    fun <T> map(mapper: Mapper<T>): T

    interface Mapper<T> {
        fun map(questions: Questions): T
    }

    class Base(
        private val readRawResource: ReadRawResource,
        private val gson: Gson,
    ) : Repository {

        override fun <T> map(mapper: Mapper<T>): T {
            val data = readRawResource.read(questionsId)
            return mapper.map(gson.fromJson(data, Questions::class.java))
        }

        companion object {
            private const val questionsId = R.raw.data
        }
    }
}

interface ReadRawResource {

    fun read(@RawRes id: Int): String

    class Base(private val context: Context) : ReadRawResource {
        override fun read(id: Int) =
            context.resources.openRawResource(id).bufferedReader().readText()
    }
}