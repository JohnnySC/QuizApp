package com.github.johnnysc.quizapp.presentation

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

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
        private val liveData: MutableLiveData<T> = SingleLiveEvent()
    ) : Communication<T> {

        override fun map(source: T) {
            liveData.value = source
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<T>) =
            liveData.observe(owner, observer)
    }
}

class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val mPending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { t ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        }
    }

    @MainThread
    override fun setValue(t: T?) {
        mPending.set(true)
        super.setValue(t)
    }
}

interface ScreenCommunication : Communication<ScreenUi> {
    class Base : Communication.Base<ScreenUi>(), ScreenCommunication
}