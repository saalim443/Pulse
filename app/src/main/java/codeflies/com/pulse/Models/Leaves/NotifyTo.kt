package codeflies.com.pulse.Models.Leaves

import com.google.gson.annotations.SerializedName

data class NotifyTo(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("selectedNotifiableUsers")
	val selectedNotifiableUsers: List<Any?>? = null,

	@field:SerializedName("users")
	val users: List<UsersItem?>? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class UsersItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("employee")
	val employee: EmployeeNotifyTo? = null,

	@field:SerializedName("email")
	val email: String? = null
)

data class ManagerUser(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)

data class EmployeeNotifyTo(
	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("manager_user")
	val managerUser: ManagerUser? = null,

	@field:SerializedName("manager_user_id")
	val managerUserId: Int? = null,

	@field:SerializedName("employee_code")
	val employeeCode: String? = null
)
