
package com.gary.httpstuff.model.response

import com.squareup.moshi.Json

/**
 * Holds the user data, to display on the profile screen.
 */
class UserProfileResponse(@field:Json(name = "email")val email: String?,
                          @field:Json(name = "name")val name: String?)