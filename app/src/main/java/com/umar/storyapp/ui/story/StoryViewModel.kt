package com.umar.storyapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.umar.storyapp.model.StoryRepository
import com.umar.storyapp.model.UserPreference
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel(
    private val userPreference: UserPreference,
    private val storyRepository: StoryRepository
) : ViewModel() {
    fun getToken(): LiveData<String> {
        return userPreference.getToken().asLiveData()
    }

    fun addStory(token: String, photo: MultipartBody.Part, desc: RequestBody) =
        storyRepository.addStories(token, photo, desc)
}