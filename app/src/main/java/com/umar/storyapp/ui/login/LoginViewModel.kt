package com.umar.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.umar.storyapp.model.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: UserPreference) : ViewModel() {


    fun setToken(token: String, isLogin: Boolean) {
        viewModelScope.launch {
            pref.setToken(token, isLogin)
        }
    }

    fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    fun login(email: String, password: String) = pref.login(email, password)
}