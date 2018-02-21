package com.thejuki.example.json

import android.os.Parcel
import android.os.Parcelable
import com.thejuki.example.enum.ObjectType

/**
 * Info Json
 *
 * Data class used in cells of the Info tab.
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
data class InfoJson(val title: String? = null,
                    val content: String? = null,
                    val id: String? = null,
                    val objectType: ObjectType? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Int::class.java.classLoader)?.let { ObjectType.values()[it as Int] }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(title)
        writeString(content)
        writeString(id)
        writeValue(objectType?.ordinal)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<InfoJson> = object : Parcelable.Creator<InfoJson> {
            override fun createFromParcel(source: Parcel): InfoJson = InfoJson(source)
            override fun newArray(size: Int): Array<InfoJson?> = arrayOfNulls(size)
        }
    }
}