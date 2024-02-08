package codeflies.com.pulse.Models.Leaves

import com.google.gson.annotations.SerializedName

data class LeavesDetails(

    @field:SerializedName("leave")
    val leave: Leave? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class Leave(

    @field:SerializedName("employeeName")
    val employeeName: String? = null,

    @field:SerializedName("leave_from")
    val leaveFrom: String? = null,

    @field:SerializedName("dateAndTime")
    val dateAndTime: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("employeeEmail")
    val employeeEmail: String? = null,

    @field:SerializedName("dateAndTimeHuman")
    val dateAndTimeHuman: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("employee")
    val employee: Employees? = null,

    @field:SerializedName("leave_partials")
    val leavePartials: List<Any?>? = null,

    @field:SerializedName("attachments")
    val attachments: List<attachmentsItem>? = null,

    @field:SerializedName("content")
    val content: String? = null,

    @field:SerializedName("users")
    val users: List<UsersItems?>? = null,

    @field:SerializedName("employeeCode")
    val employeeCode: String? = null,

    @field:SerializedName("leave_days")
    val leaveDays: Int? = null,

    @field:SerializedName("approved_by")
    val approvedBy: Any? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("leaveType")
    val leaveTypes: String? = null,

    @field:SerializedName("leave_type")
    val leaveType: String? = null,

    @field:SerializedName("approved_at")
    val approvedAt: Any? = null,

    @field:SerializedName("employee_id")
    val employeeId: Int? = null,

    @field:SerializedName("leave_end")
    val leaveEnd: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("status_reason")
    val statusReason: Any? = null,

    @field:SerializedName("notifierDetail")
    val notifierDetail: String? = null,

    @field:SerializedName("status")
    val status: String? = null
)

data class Employees(

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

    @field:SerializedName("user")
    val user: Users? = null,

    @field:SerializedName("father_profession")
    val fatherProfession: Any? = null
)

data class UsersItems(

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
    val token: String? = null
)

data class Users(

    @field:SerializedName("profile_img")
    val profileImg: String? = null,

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

data class Pivot(

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("leave_id")
    val leaveId: Int? = null
)

data class attachmentsItem(
    @field:SerializedName("id") val id: String? = null,
    @field:SerializedName("leave_id") val leave_id: String? = null,
    @field:SerializedName("file_path") val file_path: String? = null,

    )
