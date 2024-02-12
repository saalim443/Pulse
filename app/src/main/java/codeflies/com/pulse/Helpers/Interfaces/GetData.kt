package com.example.ehcf_doctor.Retrofit


import codeflies.com.pulse.Models.CandidateDetails.ResponseDetails
import codeflies.com.pulse.Models.Candidates.ResponseCandidate
import codeflies.com.pulse.Models.Holidays.ResponseHoliday
import codeflies.com.pulse.Models.Leaves.LeaveStatusDetails
import codeflies.com.pulse.Models.Leaves.LeavesDetails
import codeflies.com.pulse.Models.Leaves.NotifyTo
import codeflies.com.pulse.Models.Leaves.PartialLeaveRequest
import codeflies.com.pulse.Models.Leaves.ResponseLeaves
import codeflies.com.pulse.Models.Login.ResponseLogin
import codeflies.com.pulse.Models.ResponseNormal
import codeflies.com.pulse.Models.ResponseNotification
import codeflies.com.pulse.Models.UserData.ResponseProfile
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
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
    ): Call<ResponseDetails>

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
        @Part ("leave_notification_user_ids[]")email : MutableList<RequestBody>
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

}