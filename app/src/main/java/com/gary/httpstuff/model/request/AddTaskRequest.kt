
package com.gary.httpstuff.model.request

import com.squareup.moshi.Json

class AddTaskRequest(
    @field:Json(name = "title")val title: String,
    @field:Json(name = "content")val content: String,
    @field:Json(name = "taskPriority")val taskPriority: Int
)