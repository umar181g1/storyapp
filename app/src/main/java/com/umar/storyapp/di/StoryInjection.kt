package com.umar.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.umar.storyapp.model.StoryRepository
import com.umar.storyapp.network.Api

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object StoryInjection {
    fun provider(context: Context): StoryRepository {
        val apiSer = Api.getApiService()
        return StoryRepository.getInstance(context.dataStore, apiSer)
    }
}