package com.thejuki.example.json

import java.io.Serializable

/**
 * Contact Lookup Json
 *
 * Data class used in Contact AutoComplete.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
data class ContactLookupJson(val id: String? = null,
                             val value: String? = null,
                             val label: String? = null
) : Serializable {

    override fun toString(): String {
        return label.orEmpty()
    }
}
