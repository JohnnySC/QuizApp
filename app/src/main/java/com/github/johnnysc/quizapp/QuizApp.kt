package com.github.johnnysc.quizapp

import android.app.Application
import com.google.gson.Gson

/**
 * @author Asatryan on 31.07.2022
 */
class QuizApp : Application(), ProvideViewModel {

    private lateinit var viewModel: MainViewModel

    override fun onCreate() {
        super.onCreate()
        viewModel = MainViewModel(
            Repository.Base(
                ReadRawResource.Base(this),
                Gson()
            ),
            ScreenUiMapper.Base(
                if (BuildConfig.DEBUG)
                    ChoicesMapper.Mock()
                else
                    ChoicesMapper.Base()
            ),
            ScreenCommunication.Base()
        )
    }

    override fun provideViewModel(): MainViewModel = viewModel
}

interface ProvideViewModel {
    fun provideViewModel(): MainViewModel
}