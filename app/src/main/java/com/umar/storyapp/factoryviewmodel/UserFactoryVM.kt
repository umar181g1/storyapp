package com.umar.storyapp.factoryviewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umar.storyapp.di.UserInjection
import com.umar.storyapp.login.LoginViewModel
import com.umar.storyapp.model.UserPreference
import com.umar.storyapp.register.RegisViewModel

class UserFactoryVM private constructor(private val preference: UserPreference) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisViewModel::class.java) -> {
                RegisViewModel(preference) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(preference) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }
    companion object {
        @Volatile
        private var instance: UserFactoryVM? = null
        fun getInstance(context: Context): UserFactoryVM =
            instance ?: synchronized(this) {
                instance ?: UserFactoryVM(UserInjection.providePreferences(context))
            }.also { instance = it }
    }

}