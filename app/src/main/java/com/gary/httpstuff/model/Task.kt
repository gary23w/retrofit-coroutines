package com.gary.httpstuff.model

import kotlinx.serialization.Serializable


/**
 * Represents a task/note from the API.
 */
@Serializable
class Task(
    val id: String,
    val title: String,
    val content: String,
    val isCompleted: Boolean,
    val taskPriority: Int
)