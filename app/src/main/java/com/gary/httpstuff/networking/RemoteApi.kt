
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

    fun getTasks(onTasksReceived: (Result<List<Task>>) -> Unit) {
    apiService.getNotes().enqueue(object : Callback<GetTasksResponse> {
        override fun onFailure(call: Call<GetTasksResponse>, t: Throwable) {
            onTasksReceived(Failure(t))
        }

        override fun onResponse(call: Call<GetTasksResponse>, response: Response<GetTasksResponse>) {
            val data = response.body()
            if(data != null) {
                onTasksReceived(Success(data.notes.filter { !it.isCompleted }))
            } else {
                onTasksReceived(Failure( NullPointerException("NO DATA")))
            }
        }

    })

    }
        fun deleteTask(taskId: String, onTaskDeleted: (Result<String>) -> Unit) {
            apiService.deleteNote(taskId).enqueue(object: Callback<DeleteNoteResponse>{
                override fun onFailure(call: Call<DeleteNoteResponse>, t: Throwable) {
                    onTaskDeleted(Failure(t))
                }

                override fun onResponse(
                    call: Call<DeleteNoteResponse>,
                    response: Response<DeleteNoteResponse>
                ) {
                    val deleteNoteResponse = response.body()

                    if(deleteNoteResponse?.message == null) {
                        onTaskDeleted(Failure(NullPointerException("NO DATA")))

                    } else {
                        onTaskDeleted(Success(deleteNoteResponse.message))
                    }
                }

            })
        }

        fun completeTask(taskId: String, onTaskCompleted: (Throwable?) -> Unit) {
          apiService.completeTask(taskId).enqueue(object : Callback<CompleteNoteResponse>{
              override fun onFailure(call: Call<CompleteNoteResponse>, t: Throwable) {
                  onTaskCompleted(t)
              }

              override fun onResponse(call: Call<CompleteNoteResponse>, response: Response<CompleteNoteResponse>) {


                  val completeNoteResponse = response.body()

                  if(completeNoteResponse?.message == null) {
                      onTaskCompleted(NullPointerException("NO DATA"))
                  } else {
                      onTaskCompleted(null)
                  }
              }

          })
        }

        fun addTask(addTaskRequest: AddTaskRequest, onTaskCreated: (Result<Task>) -> Unit) {

            apiService.addTask(addTaskRequest).enqueue(object : Callback<Task>{
                override fun onFailure(call: Call<Task>, t: Throwable) {
                    onTaskCreated(Failure(t))
                }

                override fun onResponse(
                    call: Call<Task>,
                    response: Response<Task>
                ) {


                    val data = response.body()

                    if(data == null) {
                        onTaskCreated(Failure( NullPointerException("NO DATA")))

                    }else {
                        onTaskCreated(Success(data))
                    }

                }

            })
        }

        fun getUserProfile(onUserProfileReceived: (Result<UserProfile>) -> Unit) {
            getTasks { result ->
                if(result is Failure && result.error !is NullPointerException) {
                    onUserProfileReceived(Failure(result.error))
                    return@getTasks
                }

                val tasks = result as Success
                apiService.getMyProfile().enqueue(object : Callback<UserProfileResponse>{
                    override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                        onUserProfileReceived(Failure(t))
                    }

                    override fun onResponse(
                        call: Call<UserProfileResponse>,
                        response: Response<UserProfileResponse>
                    ) {

                        val userProfileResponse = response.body()

                        if(userProfileResponse?.email == null || userProfileResponse.name == null) {
                            onUserProfileReceived(Failure(NullPointerException("NO DATA")))
                        } else {
                            onUserProfileReceived(Success(UserProfile(
                                userProfileResponse.email,
                                userProfileResponse.name,
                                tasks.data.size
                            )))
                        }
                    }

                })
            }
        }
    }

