package com.github.johnnysc.quizapp

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface Mapper<R, S> {

    fun map(source: S): R

    interface Unit<T> : Mapper<kotlin.Unit, T>
}

interface SetValue<T> : Mapper.Unit<T>

interface Observe<T> {

    fun observe(owner: LifecycleOwner, observer: Observer<T>)
}

interface Communication<T> : Observe<T>, SetValue<T> {
    abstract class Base<T>(
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