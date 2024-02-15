package codeflies.com.pulse.Helpers.Interfaces


import codeflies.com.pulse.Models.CandidateDetails.CandidateDetails
import codeflies.com.pulse.Models.CandidateDetails.CandidateStatus
import codeflies.com.pulse.Models.CandidateDetails.Interviewers
import codeflies.com.pulse.Models.Candidates.ResponseCandidate
import codeflies.com.pulse.Models.Candidates.StatusList
import codeflies.com.pulse.Models.Holidays.ResponseHoliday
import codeflies.com.pulse.Models.Leaves.LeaveStatusDetails
import codeflies.com.pulse.Models.Leaves.LeavesDetails
import codeflies.com.pulse.Models.Leaves.NotifyTo
import codeflies.com.pulse.Models.Leaves.PartialLeaveRequest
import codeflies.com.pulse.Models.Leaves.ResponseLeaves
import codeflies.com.pulse.Models.Login.ResponseLogin
import codeflies.com.pulse.Models.Profile.ResponseDocuments
import codeflies.com.pulse.Models.ResponseNormal
import codeflies.com.pulse.Models.ResponseNotification
import codeflies.com.pulse.Models.UserData.ResponseProfile
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface GetData {


    @POST("api/login")
    fun login(
        @Query("email") email: String?,
        @Query("password") password: String?,
        @Query("fcm_token") fcm_token: String?,
    ): Call<ResponseLogin>

    @POST("api/leaves/status/update")
    fun statusUpdate(
        @Header("Authorization") token: String?,
        @Body request: PartialLeaveRequest
    ): Call<ResponseNormal>

    @GET("api/leaves/list")
    fun leaves(
        @Header("Authorization") token: String?,
    ): Call<ResponseLeaves>


    @Headers("Accept: application/json")
    @GET("api/leaves/leave-detail/{leave_id}")
    fun getLeaveData(
        @Header("Authorization") authorization: String,
        @Path("leave_id") leave_id: String
    ): Call<LeavesDetails>


    @GET("api/recruitment/candidates")
    fun candidates(
        @Header("Authorization") token: String?,
    ): Call<ResponseCandidate>


    @GET("api/recruitment/candidates/detail/{id}")
    fun candidatesDetails(
        @Header("Authorization") token: String?,
        @Path("id") id: String?,
    ): Call<CandidateDetails>

    @GET("api/holidays/list")
    fun holiday(
        @Header("Authorization") token: String?,
    ): Call<ResponseHoliday>

    @GET("api/user")
    fun profile(
        @Header("Authorization") token: String?
    ): Call<ResponseProfile>

    @POST("api/logout")
    fun logout(
        @Header("Authorization") token: String?
    ): Call<ResponseNormal>


    @GET("api/notifications/list")
    fun notification(
        @Header("Authorization") token: String?,
    ): Call<ResponseNotification>

    @GET("api/notifications/read-all")
    fun readNotification(
        @Header("Authorization") token: String?,
    ): Call<ResponseNormal>


    @Headers("Accept: application/json")
    @GET("api/leaves/notify-to")
    fun notify_to(
        @Header("Authorization") token: String?,
    ): Call<NotifyTo>

    @Headers("Accept: application/json")
    @GET("api/leaves/status")
    fun getStatus(
        @Header("Authorization") token: String?,
    ): Call<LeaveStatusDetails>


    @Multipart
    @Headers("Accept: application/json")
    @POST("api/leaves/leave-apply")
    fun uploadLeave(
        @Header("Authorization") authorization: String,
        @Part("title") title: RequestBody,
        @Part("leave_type") leave_type: RequestBody,
        @Part("leave_from_date") leave_from_date: RequestBody,
        @Part("leave_to_date") leave_to_date: RequestBody,
        @Part("leave_days") leave_days: RequestBody,
        @Part("content") content: RequestBody,
        @Part attachments: List<MultipartBody.Part>,
        @Part("leave_notification_user_ids[]") email: MutableList<RequestBody>
    ): Call<ResponseNormal>


    @Multipart
    @Headers("Accept: application/json")
    @POST("/api/recruitment/candidates/add")
    fun uploadCandidate(
        @Header("Authorization") authorization: String,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part("alternate_mobile") alternate_mobile: RequestBody,
        @Part("designation") designation: RequestBody,
        @Part("experience_years") experience_years: RequestBody,
        @Part("experience_months") experience_months: RequestBody,
        @Part("notice_period_left") notice_period_left: RequestBody,
        @Part("expected_salary") expected_salary: RequestBody,
        @Part("current_salary") current_salary: RequestBody,
        @Part("status") status: RequestBody,
        @Part("remarks") remarks: RequestBody,
        @Part("recruiter_id") recruiter: RequestBody,
        @Part resume: MultipartBody.Part
    ): Call<ResponseNormal>


    @Multipart
    @Headers("Accept: application/json")
    @POST("api/employee/update")
    fun uploadDetails(
        @Header("Authorization") authorization: String,
        @Part("alternate_mobile") alternate_mobile: RequestBody,
        @Part("father_profession") father_profession: RequestBody,
        @Part("date_of_birth") date_of_birth: RequestBody,
        @Part("mother_name") mother_name: RequestBody,
        @Part("mother_profession") mother_profession: RequestBody,
        @Part("temporary_addr") temporary_addr: RequestBody,
        @Part attachments: MultipartBody.Part?
    ): Call<ResponseNormal>

    @Multipart
    @Headers("Accept: application/json")
    @POST("api/user/profile")
    fun uploadAdmin(
        @Header("Authorization") authorization: String,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("mobile") mobile: RequestBody,
        @Part attachments: MultipartBody.Part?
    ): Call<ResponseNormal>


    @GET("api/employee/documents/list")
    fun documents(
        @Header("Authorization") token: String?
    ): Call<ResponseDocuments>


    @Multipart
    @Headers("Accept: application/json")
    @POST("api/employee/documents/store")
    fun uploadDocuments(
        @Header("Authorization") authorization: String,
        @Part("doc_type") doc_type: RequestBody,
        @Part("title") title: RequestBody,
        @Part("remarks") remarks: RequestBody,
        @Part attachments: MultipartBody.Part
    ): Call<ResponseNormal>


    @POST("api/user/reset/password")
    fun resetPassword(
        @Header("Authorization") authorization: String,
        @Query("old_password") old_password: String?,
        @Query("password") password: String?,
        @Query("password_confirmation") password_confirmation: String?,
    ): Call<ResponseNormal>


    @Headers("Accept: application/json")
    @GET("api/recruitment/candidates/status")
    fun getCandidateStatus(
        @Header("Authorization") token: String?,
    ): Call<CandidateStatus>

    @Headers("Accept: application/json")
    @GET("api/recruitment/interviewers/list")
    fun getInterviewers(
        @Header("Authorization") token: String?,
    ): Call<Interviewers>



    @POST("api/recruitment/candidates/round/store")
    fun addInterviewRound(
        @Header("Authorization") authorization: String,
        @Query("user_id") user_id: String?,
        @Query("candidate_id") candidate_id: String?,
        @Query("interview_at") interview_at: String?,
        @Query("round") round: String?,
        @Query("status") status: String?,
    ): Call<ResponseNormal>

    @Multipart
    @Headers("Accept: application/json")
    @POST("api/recruitment/candidates/comment/store")
    fun addComment(
        @Header("Authorization") authorization: String,
        @Part("comment") comment: RequestBody,
        @Part("candidate_id") candidate_id: RequestBody,
        @Part attachments: MultipartBody.Part?
    ): Call<ResponseNormal>



    @Headers("Accept: application/json")
    @GET("api/recruitment/candidates/status")
    fun getCandidateStatusList(
        @Header("Authorization") token: String?,
    ): Call<StatusList>




    @GET("api/recruitment/candidates/status/update/{id}")
    fun updateCondidateStatus(
        @Header("Authorization") authorization: String,
        @Path("id") id: String?,
        @Query("status") status: String?,
    ): Call<ResponseNormal>
}