package com.gary.httpstuff.networking

import com.gary.httpstuff.model.Task
import com.gary.httpstuff.model.request.AddTaskRequest
import com.gary.httpstuff.model.request.UserDataRequest
import com.gary.httpstuff.model.response.*
import retrofit2.Call
import retrofit2.http.*

interface RemoteApiService {

    @POST("api/register")
    fun registerUser(@Body request: UserDataRequest): Call<RegisterResponse>

    @GET("/api/note")
    fun getNotes(@Header("Authorization") token: String): Call<GetTasksResponse>

    @POST("api/login")
    fun loginUser(@Body request: UserDataRequest): Call<LoginResponse>

    @GET("api/user/profile")
    fun getMyProfile(@Header("Authorization") token:  String): Call<UserProfileResponse>

    @POST("/api/note/complete")
    fun completeTask(@Header("Authorization") token: String,
    @Query("id") noteId: String): Call<CompleteNoteResponse>

    @POST("/api/note")
    fun addTask(@Header("Authorization") token: String,
                @Body request: AddTaskRequest
    ): Call<Task>
}