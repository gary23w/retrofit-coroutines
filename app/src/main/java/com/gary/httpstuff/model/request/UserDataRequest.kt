

package com.gary.httpstuff.model.request

import com.squareup.moshi.Json

data class UserDataRequest(
    @field:Json(name = "username") val username: String? = null,
    @field:Json(name = "email") val email: String,
    @field:Json(name = "password")val password: String


)