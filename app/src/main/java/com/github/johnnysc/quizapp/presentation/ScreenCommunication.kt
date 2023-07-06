package com.github.johnnysc.quizapp.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface Mapper<R : Any, S : Any> {

    fun map(source: S): R

    interface Unit<T : Any> : Mapper<kotlin.Unit, T>
}

interface SetValue<T : Any> : Mapper.Unit<T>

interface Observe<T : Any> {

    fun observe(owner: LifecycleOwner, observer: Observer<T>) = Unit
}

interface Communication<T : Any> : Observe<T>, SetValue<T> {

    abstract class Base<T : Any>(
        private val liveData: MutableLiveData<T> = MutableLiveData()
    ) : Communication<T> {

        override fun map(source: T) {
            liveData.value = source
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<T>) =
            liveData.observe(owner, observer)
    }
}

interface ScreenCommunication : Communication<ScreenUi> {
    class Base : Communication.Base<ScreenUi>(), ScreenCommunication
}