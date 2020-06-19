
package com.gary.httpstuff.model.response

import kotlinx.serialization.Serializable

@Serializable

/**
 * Holds the user data, to display on the profile screen.
 */
class UserProfileResponse(val email: String?,
                         val name: String?)