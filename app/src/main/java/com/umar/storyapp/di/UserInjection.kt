package com.umar.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.umar.storyapp.model.UserPreference
import com.umar.storyapp.network.Api


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object UserInjection {
    fun providePreferences(context: Context) : UserPreference{
        val apiSer = Api.getApiService()
        return UserPreference.getInstance(context.dataStore, apiSer)
    }
}