package com.wem.snoozy.data.remote

import com.wem.snoozy.data.remote.dto.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/v1/auth/basic/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("api/v1/auth/basic/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("api/v1/auth/google")
    suspend fun googleAuth(
        @Body request: GoogleAuthRequest
    ): Response<AuthResponse>

    // Groups
    @GET("api/v1/groups")
    suspend fun getGroups(): Response<List<GroupResponse>>

    @GET("api/v1/groups/{id}")
    suspend fun getGroupById(
        @Path("id") id: Int
    ): Response<GroupResponse>

    @POST("api/v1/groups")
    suspend fun createGroup(
        @Body request: CreateGroupRequest
    ): Response<GroupResponse>

    @Multipart
    @POST("api/v1/groups/{id}")
    suspend fun uploadGroupAvatar(
        @Path("id") id: Int,
        @Part file: MultipartBody.Part
    ): Response<AvatarResponse>

    // Alarms
    @GET("api/v1/alarms")
    suspend fun getAlarms(): Response<List<RemoteAlarmDto>>

    @POST("api/v1/alarms")
    suspend fun createAlarm(
        @Body request: CreateAlarmRequest
    ): Response<RemoteAlarmDto>

    @PATCH("api/v1/alarms/{alarmId}")
    suspend fun updateAlarm(
        @Path("alarmId") alarmId: Long,
        @Body request: @JvmSuppressWildcards Map<String, Any>
    ): Response<RemoteAlarmDto>

    @DELETE("api/v1/alarms/{alarmId}")
    suspend fun deleteAlarm(
        @Path("alarmId") alarmId: Long
    ): Response<Unit>
}
