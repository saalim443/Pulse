package codeflies.com.pulse.Models.Leaves

import com.google.gson.annotations.SerializedName

data class ResponseLeaves(

	@field:SerializedName("leaves")
	val leaves: ArrayList<LeavesItem>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("employees")
	val employees: List<Any?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class User(

	@field:SerializedName("profile_img")
	val profileImg: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mobile")
	val mobile: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)

data class Employee(

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("employee_code")
	val employeeCode: String? = null
)

data class ApprovedBy(

	@field:SerializedName("profile_img")
	val profileImg: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mobile")
	val mobile: String? = null,

	@field:SerializedName("fcm_token")
	val fcmToken: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("email_verified_at")
	val emailVerifiedAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: Int? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class LeavesItem(

	@field:SerializedName("leave_from")
	val leaveFrom: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("employee")
	val employee: Employee? = null,

	@field:SerializedName("content")
	val content: String? = null,

	@field:SerializedName("leave_days")
	val leaveDays: Double? = null,

	@field:SerializedName("approved_by")
	val approvedBy: ApprovedBy? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("leave_type")
	val leaveType: String? = null,

	@field:SerializedName("approved_at")
	val approvedAt: String? = null,

	@field:SerializedName("employee_id")
	val employeeId: Int? = null,

	@field:SerializedName("leave_end")
	val leaveEnd: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("status_reason")
	val statusReason: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
