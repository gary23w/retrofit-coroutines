

package com.gary.httpstuff.model.response

import com.gary.httpstuff.model.Task
import kotlinx.serialization.Serializable


@Serializable
data class GetTasksResponse( val notes: List<Task> = mutableListOf())