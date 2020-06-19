

package com.gary.httpstuff.model.response

import com.squareup.moshi.Json

data class LoginResponse( @field:Json(name = "token")val token: String? = "")