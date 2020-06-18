
package com.gary.httpstuff.model

/**
 * Represents a task/note from the API.
 */
class Task(
    val id: String,
    val title: String,
    val content: String,
    val isCompleted: Boolean,
    val taskPriority: Int
)