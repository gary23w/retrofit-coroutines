

package com.gary.httpstuff.model.request

import kotlinx.serialization.Serializable


@Serializable
data class UserDataRequest(
    val username: String? = null,
    val email: String,
    val password: String


)