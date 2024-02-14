package codeflies.com.pulse.Models.CandidateDetails

import com.google.gson.annotations.SerializedName

data class Interviewers(

	@field:SerializedName("interviewers")
	val interviewers: List<InterviewersItem?>? = null,

	@field:SerializedName("roundStatus")
	val roundStatus: List<RoundStatusItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null,

	@field:SerializedName("roundTypes")
	val roundTypes: List<RoundTypesItem?>? = null
)

data class InterviewersItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class RoundStatusItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("slug")
	val slug: String? = null
)

data class RoundTypesItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("slug")
	val slug: String? = null
)
