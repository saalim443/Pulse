package codeflies.com.pulse.Models.Leaves

import com.google.gson.annotations.SerializedName

data class LeaveStatusDetails(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("leaveStatus")
	val leaveStatus: List<LeaveStatusItem?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class LeaveStatusItem(

	@field:SerializedName("color")
	val color: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("theme")
	val theme: String? = null,

	@field:SerializedName("slug")
	val slug: String? = null
)
