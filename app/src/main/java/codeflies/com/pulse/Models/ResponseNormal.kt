package codeflies.com.pulse.Models

import com.google.gson.annotations.SerializedName

data class ResponseNormal(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)
