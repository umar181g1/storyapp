package com.umar.storyapp.register

import androidx.lifecycle.ViewModel
import com.umar.storyapp.model.UserPreference

class RegisViewModel(private val pref: UserPreference) : ViewModel() {
    fun register(name: String, email: String, password: String) = pref.register(name, email, password)
}