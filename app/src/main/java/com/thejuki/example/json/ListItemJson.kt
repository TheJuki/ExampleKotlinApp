package com.thejuki.example.json

import java.io.Serializable

/**
 * List Item Json
 *
 * Data class used in Dropdowns.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
data class ListItemJson(val id: String? = null,
                        val name: String? = null
) : Serializable {
    override fun toString(): String {
        return name.orEmpty()
    }
}