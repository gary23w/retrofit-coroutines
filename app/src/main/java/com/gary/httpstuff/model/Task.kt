
package com.gary.httpstuff.model

import com.squareup.moshi.Json

/**
 * Represents a task/note from the API.
 */
class Task(
    @field:Json(name = "id")val id: String,
    @field:Json(name = "title")val title: String,
    @field:Json(name = "content")val content: String,
    @field:Json(name = "isCompleted")val isCompleted: Boolean,
    @field:Json(name = "taskPriority")val taskPriority: Int
)