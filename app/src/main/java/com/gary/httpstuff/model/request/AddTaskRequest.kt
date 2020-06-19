
package com.gary.httpstuff.model.request

import kotlinx.serialization.Serializable


@Serializable
class AddTaskRequest(
   val title: String,
   val content: String,
   val taskPriority: Int
)