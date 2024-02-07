package com.example.ehcf_doctor.Retrofit


import codeflies.com.pulse.Models.Candidates.ResponseCandidate
import codeflies.com.pulse.Models.Holidays.ResponseHoliday
import codeflies.com.pulse.Models.Leaves.ResponseLeaves
import codeflies.com.pulse.Models.Login.ResponseLogin
import codeflies.com.pulse.Models.UserData.ResponseProfile
import retrofit2.Call
import retrofit2.http.*


interface GetData {


    @POST("api/login")
    fun login(
        @Query("email") email: String?,
        @Query("password") password: String?,
        @Query("fcm_token") fcm_token: String?,
         ): Call<ResponseLogin>

    @GET("api/leaves/list")
    fun leaves(
        @Header("Authorization") token: String?,
    ): Call<ResponseLeaves>

    @GET("api/recruitment/candidates")
    fun candidates(
        @Header("Authorization") token: String?,
    ): Call<ResponseCandidate>

    @GET("api/holidays/list")
    fun holiday(
        @Header("Authorization") token: String?,
    ): Call<ResponseHoliday>

    @GET("api/user/{user_id}")
    fun profile(
        @Header("Authorization") token: String?,
        @Path("user_id") user_id: String?,
    ): Call<ResponseProfile>


}