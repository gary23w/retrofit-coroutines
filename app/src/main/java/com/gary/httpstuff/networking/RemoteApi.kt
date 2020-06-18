
package com.gary.httpstuff.networking

import com.gary.httpstuff.model.Task
import com.gary.httpstuff.model.UserProfile
import com.gary.httpstuff.model.request.AddTaskRequest
import com.gary.httpstuff.model.request.UserDataRequest


/**
 * Holds decoupled logic for all the API calls.
 */

const val BASE_URL = "https://taskie-rw.herokuapp.com"

class RemoteApi {

  fun loginUser(userDataRequest: UserDataRequest, onUserLoggedIn: (String?, Throwable?) -> Unit) {
    onUserLoggedIn("token", null)
  }

  fun registerUser(userDataRequest: UserDataRequest, onUserCreated: (String?, Throwable?) -> Unit) {
    onUserCreated("Success!", null)
  }

  fun getTasks(onTasksReceived: (List<Task>, Throwable?) -> Unit) {
    onTasksReceived(listOf(
        Task("id",
            "Wash laundry",
            "Wash the whites and colored separately!",
            false,
            1
        ),
        Task("id2",
            "Do some work",
            "Finish the project",
            false,
            3
        )
    ), null)
  }

  fun deleteTask(onTaskDeleted: (Throwable?) -> Unit) {
    onTaskDeleted(null)
  }

  fun completeTask(onTaskCompleted: (Throwable?) -> Unit) {
    onTaskCompleted(null)
  }

  fun addTask(addTaskRequest: AddTaskRequest, onTaskCreated: (Task?, Throwable?) -> Unit) {
    onTaskCreated(
        Task("id3",
            addTaskRequest.title,
            addTaskRequest.content,
            false,
            addTaskRequest.taskPriority
        ), null
    )
  }

  fun getUserProfile(onUserProfileReceived: (UserProfile?, Throwable?) -> Unit) {
    onUserProfileReceived(UserProfile("mail@mail.com", "Filip", 10), null)
  }
}