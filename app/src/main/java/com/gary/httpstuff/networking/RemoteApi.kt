
package com.gary.httpstuff.networking


import com.gary.httpstuff.model.*
import com.gary.httpstuff.model.request.AddTaskRequest
import com.gary.httpstuff.model.request.UserDataRequest
import com.gary.httpstuff.model.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException


const val BASE_URL = "https://taskie-rw.herokuapp.com"

class RemoteApi(private val apiService: RemoteApiService) {

    fun loginUser(userDataRequest: UserDataRequest, onUserLoggedIn: (Result<String>) -> Unit) {
        apiService.loginUser(userDataRequest).enqueue(object : Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onUserLoggedIn(Failure(t))
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val loginResponse = response.body()

                if(loginResponse == null || loginResponse.token.isNullOrBlank()) {
                    onUserLoggedIn(Failure( NullPointerException("NO DATA")))
                } else {
                    onUserLoggedIn(Success(loginResponse.token))
                }
            }

        })
    }

    fun registerUser(userDataRequest: UserDataRequest, onUserCreated: (Result<String>) -> Unit) {
        apiService.registerUser(userDataRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                onUserCreated(Failure(t))
            }
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                val message = response.body()?.message
                if(message == null) {
                    onUserCreated(Failure(NullPointerException("NO RESPONSE BODY")))
                    return
                }
                onUserCreated(Success(message))
            }

        })
    }

    suspend fun getTasks(): Result<List<Task>> = try {
        val data = apiService.getNotes()
        Success(data.notes.filter { !it.isCompleted })
    } catch (error: Throwable) {
        Failure(error)
    }
        suspend fun deleteTask(taskId: String): Result<String> =  try {
              val data = apiService.deleteNote(taskId)
              Success(data.message)
          } catch (error: Throwable) {
              Failure(error)
          }


        suspend fun completeTask(taskId: String): Result<String> = try {
            val data = apiService.completeTask(taskId)
            Success(data.message!!)
        } catch (error: Throwable) {
            Failure(error)
        }


        suspend fun addTask(addTaskRequest: AddTaskRequest): Result<Task> = try {
            val data = apiService.addTask(addTaskRequest)
            Success(data)
        } catch (error: Throwable) {
            Failure(error)
        }

        suspend fun getUserProfile(): Result<UserProfile> = try {
        val notesResult = getTasks()

            if(notesResult is Failure) {
                Failure(notesResult.error)
            } else {
                val notes = notesResult as Success
                val data = apiService.getMyProfile()

                if (data.email == null || data.name == null) {
                    Failure(NullPointerException("No data"))
                } else {
                    Success(UserProfile(data.email, data.name, notes.data.size))
                }
            }

        } catch (error: Throwable) {
        Failure(error)
        }

    }

