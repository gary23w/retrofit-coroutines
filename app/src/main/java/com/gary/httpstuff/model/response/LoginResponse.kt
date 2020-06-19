

package com.gary.httpstuff.model.response

import kotlinx.serialization.Serializable

@Serializable

data class LoginResponse(val token: String? = "")