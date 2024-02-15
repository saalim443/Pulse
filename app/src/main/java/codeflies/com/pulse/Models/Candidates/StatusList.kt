package codeflies.com.pulse.Models.Candidates

import com.google.gson.annotations.SerializedName

data class StatusList(

	@field:SerializedName("candidateStatus")
	val candidateStatus: List<CandidateStatusItem>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class CandidateStatusItem(

	@field:SerializedName("color")
	val color: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("dark")
	val dark: Boolean? = null,

	@field:SerializedName("slug")
	val slug: String? = null
)
