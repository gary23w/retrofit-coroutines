
package com.gary.httpstuff.networking


import com.gary.httpstuff.App
import com.gary.httpstuff.model.Task
import com.gary.httpstuff.model.UserProfile
import com.gary.httpstuff.model.request.AddTaskRequest
import com.gary.httpstuff.model.request.UserDataRequest
import com.gary.httpstuff.model.response.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.NullPointerException


const val BASE_URL = "https://taskie-rw.herokuapp.com"

class RemoteApi(private val apiService: RemoteApiService) {



    fun loginUser(userDataRequest: UserDataRequest, onUserLoggedIn: (String?, Throwable?) -> Unit) {
        apiService.loginUser(userDataRequest).enqueue(object : Callback<LoginResponse>{
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onUserLoggedIn(null, t)
            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val loginResponse = response.body()

                if(loginResponse == null || loginResponse.token.isNullOrBlank()) {
                    onUserLoggedIn(null, NullPointerException("NO DATA"))
                } else {
                    onUserLoggedIn(loginResponse.token, null)
                }
            }

        })
    }

    fun registerUser(userDataRequest: UserDataRequest, onUserCreated: (String?, Throwable?) -> Unit) {
        apiService.registerUser(userDataRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                onUserCreated(null, t)
            }
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                val message = response.body()?.message
                if(message == null) {
                    onUserCreated(null, NullPointerException("NO RESPONSE BODY"))
                    return
                }
                onUserCreated(message, null)
            }

        })
    }

    fun getTasks(onTasksReceived: (List<Task>, Throwable?) -> Unit) {
    apiService.getNotes(App.getToken()).enqueue(object : Callback<GetTasksResponse> {
        override fun onFailure(call: Call<GetTasksResponse>, t: Throwable) {
            onTasksReceived(emptyList(), t)
        }

        override fun onResponse(call: Call<GetTasksResponse>, response: Response<GetTasksResponse>) {
            val data = response.body()
            if(data != null) {
                onTasksReceived(data.notes.filter { !it.isCompleted }, null)
            } else {
                onTasksReceived(emptyList(), NullPointerException("NO DATA"))
            }
        }

    })

    }
        fun deleteTask(onTaskDeleted: (Throwable?) -> Unit) {
            onTaskDeleted(null)
        }

        fun completeTask(taskId: String, onTaskCompleted: (Throwable?) -> Unit) {
          apiService.completeTask(App.getToken(), taskId).enqueue(object : Callback<CompleteNoteResponse>{
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

        fun addTask(addTaskRequest: AddTaskRequest, onTaskCreated: (Task?, Throwable?) -> Unit) {

            apiService.addTask(App.getToken(), addTaskRequest).enqueue(object : Callback<Task>{
                override fun onFailure(call: Call<Task>, t: Throwable) {
                    onTaskCreated(null, t)
                }

                override fun onResponse(
                    call: Call<Task>,
                    response: Response<Task>
                ) {


                    val data = response.body()

                    if(data == null) {
                        onTaskCreated(null, NullPointerException("NO DATA"))

                    }else {
                        onTaskCreated(data, null)
                    }

                }

            })
        }

        fun getUserProfile(onUserProfileReceived: (UserProfile?, Throwable?) -> Unit) {
            getTasks { task, error ->
                if(error != null && error !is NullPointerException) {
                    onUserProfileReceived(null, error)
                    return@getTasks
                }
                apiService.getMyProfile(App.getToken()).enqueue(object : Callback<UserProfileResponse>{
                    override fun onFailure(call: Call<UserProfileResponse>, t: Throwable) {
                        onUserProfileReceived(null, error)
                    }

                    override fun onResponse(
                        call: Call<UserProfileResponse>,
                        response: Response<UserProfileResponse>
                    ) {

                        val userProfileResponse = response.body()

                        if(userProfileResponse?.email == null || userProfileResponse.name == null) {
                            onUserProfileReceived(null, error)
                        } else {
                            onUserProfileReceived(UserProfile(
                                userProfileResponse.email,
                                userProfileResponse.name,
                                task.size
                            ), null)
                        }
                    }

                })
            }
        }
    }

