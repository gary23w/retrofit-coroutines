

package com.gary.httpstuff.model.request

data class UserDataRequest(val email: String, val password: String, val name: String? = null)