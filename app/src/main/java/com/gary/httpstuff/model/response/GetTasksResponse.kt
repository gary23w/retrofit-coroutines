

package com.gary.httpstuff.model.response

import com.gary.httpstuff.model.Task


data class GetTasksResponse(val notes: List<Task> = mutableListOf())