package com.umar.storyapp.factoryviewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umar.storyapp.di.StoryInjection
import com.umar.storyapp.di.UserInjection
import com.umar.storyapp.model.StoryRepository
import com.umar.storyapp.model.UserPreference
import com.umar.storyapp.ui.main.MainViewModel
import com.umar.storyapp.ui.story.StoryViewModel

class StroyFactoryViewModel private constructor(
    private val userPreference: UserPreference,
    private val storyRepository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userPreference, storyRepository) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(userPreference, storyRepository) as T
            }
            else -> {
                throw  IllegalArgumentException("uknow viewmodel class:" + modelClass.name)
            }
        }
    }

    companion object {
        private var instance: StroyFactoryViewModel? = null
        fun getInstance(context: Context): StroyFactoryViewModel =
            instance ?: synchronized(this) {
                instance ?: StroyFactoryViewModel(
                    UserInjection.providePreferences(context),
                    StoryInjection.provider(context)

                )
            }.also { instance = it }
    }
}