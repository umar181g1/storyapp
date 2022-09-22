package com.umar.storyapp.network

import com.umar.storyapp.model.ResponseAll
import com.umar.storyapp.model.ResponseLogin
import com.umar.storyapp.model.ResponseRegis
import com.umar.storyapp.model.ResponseUpload
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseLogin

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): ResponseRegis


    @GET("stories")
    suspend fun getStories(@Header("Authorization") token: String): ResponseAll

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Header("Authorization") Authorization: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): ResponseUpload


}