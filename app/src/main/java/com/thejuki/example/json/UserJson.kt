package com.thejuki.example.json

/**
 * User Json
 *
 * Data class to get details about the user after login.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
data class UserJson(val Success: Boolean?,
                    val token: String?,
                    val username: String?,
                    val id: String?,
                    val fullName: String?,
                    val userPreferences: String?,
                    val roles: Set<String>
)