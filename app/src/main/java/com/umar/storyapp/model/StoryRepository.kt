package com.umar.storyapp.model

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.umar.storyapp.network.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService
) {
    fun getStories(token: String): LiveData<Result<ResponseAll>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.getStories("Bearer $token")
            emit(Result.Success(result))
        } catch (e: Exception) {
            Log.d("StoryRepository", "getStories: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun addStories(
        token: String,
        photo: MultipartBody.Part,
        desc: RequestBody
    ): LiveData<Result<ResponseUpload>> = liveData {
        emit(Result.Loading)
        try {
            val result = apiService.addStory("Bearer $token", photo, desc)
            emit(Result.Success(result))
        } catch (e: Exception) {
            Log.d("StoryRepository", "addStories: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: StoryRepository? = null

        fun getInstance(
            dataStore: DataStore<Preferences>,
            apiService: ApiService
        ): StoryRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = StoryRepository(apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}