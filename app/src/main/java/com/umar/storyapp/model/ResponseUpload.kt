package com.umar.storyapp.model

import com.google.gson.annotations.SerializedName

data class ResponseUpload(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
