package codeflies.com.pulse.Models.Login

import com.google.gson.annotations.SerializedName

data class ResponseLogin(

	@field:SerializedName("permissions")
	val permissions: List<String?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class DocumentsItem(

	@field:SerializedName("file_path")
	val filePath: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("remarks")
	val remarks: String? = null
)

data class Employee(

	@field:SerializedName("joining_date")
	val joiningDate: String? = null,

	@field:SerializedName("pf_percent")
	val pfPercent: Any? = null,

	@field:SerializedName("full_n_final")
	val fullNFinal: Any? = null,

	@field:SerializedName("date_of_birth")
	val dateOfBirth: String? = null,

	@field:SerializedName("mother_name")
	val motherName: String? = null,

	@field:SerializedName("alternate_mobile")
	val alternateMobile: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("permanent_addr")
	val permanentAddr: String? = null,

	@field:SerializedName("manager_user_id")
	val managerUserId: Int? = null,

	@field:SerializedName("employee_code")
	val employeeCode: String? = null,

	@field:SerializedName("temporary_addr")
	val temporaryAddr: Any? = null,

	@field:SerializedName("exit_reason")
	val exitReason: Any? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("father_name")
	val fatherName: String? = null,

	@field:SerializedName("mother_profession")
	val motherProfession: Any? = null,

	@field:SerializedName("exit_date")
	val exitDate: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("designation")
	val designation: String? = null,

	@field:SerializedName("father_profession")
	val fatherProfession: Any? = null,

	@field:SerializedName("managers")
	val managers: List<ManagersItem?>? = null
)

data class PermissionsItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("pivot")
	val pivot: Pivot? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class RolesItem(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("permissions")
	val permissions: List<PermissionsItem?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("pivot")
	val pivot: Pivot? = null,

	@field:SerializedName("id")
	val id: Int? = null
)

data class ManagersItem(

	@field:SerializedName("profile_img")
	val profileImg: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("mobile")
	val mobile: String? = null,

	@field:SerializedName("fcm_token")
	val fcmToken: Any? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("pivot")
	val pivot: Pivot? = null,

	@field:SerializedName("email_verified_at")
	val emailVerifiedAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: Int? = null,

	@field:SerializedName("token")
	val token: Any? = null
)

data class SalariesItem(

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("remarks")
	val remarks: String? = null
)

data class Pivot(

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("role_id")
	val roleId: Int? = null,

	@field:SerializedName("permission_id")
	val permissionId: Int? = null,

	@field:SerializedName("employee_id")
	val employeeId: Int? = null
)

data class CurrentSalary(

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("remarks")
	val remarks: String? = null
)

data class User(

	@field:SerializedName("documents")
	val documents: List<DocumentsItem?>? = null,

	@field:SerializedName("roles")
	val roles: List<RolesItem?>? = null,

	@field:SerializedName("mobile")
	val mobile: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("email_verified_at")
	val emailVerifiedAt: String? = null,

	@field:SerializedName("current_salary")
	val currentSalary: CurrentSalary? = null,

	@field:SerializedName("employee")
	val employee: Employee? = null,

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("leave_records")
	val leaveRecords: List<Any?>? = null,

	@field:SerializedName("profile_img")
	val profileImg: String? = null,

	@field:SerializedName("primaryRole")
	val primaryRole: String? = null,

	@field:SerializedName("assigned_leads")
	val assignedLeads: List<Any?>? = null,

	@field:SerializedName("salaries")
	val salaries: List<SalariesItem?>? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("fcm_token")
	val fcmToken: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
