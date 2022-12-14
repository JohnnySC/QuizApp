package com.github.johnnysc.quizapp

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel

/**
 * @author Asatryan on 31.07.2022
 */
class MainViewModel(
    repository: Repository,
    screenUiMapper: ScreenUiMapper,
    private val communication: ScreenCommunication
) : ViewModel(), Observe<ScreenUi>, NextAction, ChoiceButtonClickCallback {

    private var screenUi: ScreenUi = repository.map(screenUiMapper)

    init {
        communication.map(screenUi)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<ScreenUi>) =
        communication.observe(owner, observer)

    override fun goToNextScreen() {
        screenUi = screenUi.next()
        communication.map(screenUi)
    }

    val liveData = MutableLiveData<String>()

    override fun finish() {//todo replace with navigation to other screen
        liveData.value = "finished"
    }

    private var corrects = 0
    private var incorrects = 0

    override fun correctClicked() {
        screenUi.updateState(ScreenUi.State.ChoiceMade)
        communication.map(screenUi)
        corrects++
        liveData.value = "corrects $corrects"
    }

    override fun incorrectClicked() {
        screenUi.updateState(ScreenUi.State.ChoiceMade)
        communication.map(screenUi)
        incorrects++
        liveData.value = "incorrects $incorrects"
    }
}