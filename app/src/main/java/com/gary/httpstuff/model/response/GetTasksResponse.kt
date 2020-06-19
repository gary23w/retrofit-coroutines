

package com.gary.httpstuff.model.response

import com.gary.httpstuff.model.Task
import com.squareup.moshi.Json


data class GetTasksResponse( @field:Json(name = "notes")val notes: List<Task> = mutableListOf())