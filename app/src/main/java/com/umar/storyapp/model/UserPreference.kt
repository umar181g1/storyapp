package com.umar.storyapp.model

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.umar.storyapp.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>, private val apiService: ApiService){

    fun login(email: String, password: String) : LiveData<Result<ResponseLogin>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.login(email, password)
            emit(Result.Success(result))
        } catch (e: Exception) {
            Log.d("UserRepository", "Login: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }



    fun register(name: String, email: String, password: String) : LiveData<Result<ResponseRegis>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.register(name, email, password)
            emit(Result.Success(result))
        } catch (e: Exception) {
            Log.d("UserRepository", "Register: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }
    }

    suspend fun setToken(token: String, isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
            preferences[STATE_KEY] = isLogin.toString()
        }
    }

    fun isLogin(): Flow<Any> {
        return dataStore.data.map { preferences ->
            preferences[STATE_KEY] ?: false
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[TOKEN] = ""
            preferences[STATE_KEY] = false.toString()
        }
    }


    companion object{
        @Volatile
        private var INSTANCE: UserPreference? =null

        private val  TOKEN = stringPreferencesKey("token")
        private val STATE_KEY = stringPreferencesKey("statce")

        fun getInstance(dataStore: DataStore<Preferences>, apiService: ApiService): UserPreference {
            return INSTANCE ?: synchronized(this){
                val instance = UserPreference(dataStore,apiService)
                INSTANCE = instance
                instance
            }
        }
    }

}