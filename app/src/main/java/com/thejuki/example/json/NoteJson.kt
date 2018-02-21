package com.thejuki.example.json

import android.os.Parcel
import android.os.Parcelable

/**
 * Note Json
 *
 * Data class used for Note details and form
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
data class NoteJson(var id: String? = null,
                    var type: String? = null,
                    var body: String? = null,
                    var parentId: String? = null,
                    var parentType: String? = null,
                    var modifyingUser: String? = null,
                    var creatingUser: String? = null,
                    var createdDate: String? = null,
                    var modifiedDate: String? = null,
                    var formAction: String? = null
) : Parcelable {
    fun getInfos(): ArrayList<InfoJson> {
        val infoJsons = ArrayList<InfoJson>()

        infoJsons.add(InfoJson("Number", id.orEmpty()))
        infoJsons.add(InfoJson("Parent Object", "${parentType.orEmpty()} (ID: ${parentId.orEmpty()})"))
        infoJsons.add(InfoJson("Type", type.orEmpty()))
        infoJsons.add(InfoJson("", ""))
        infoJsons.add(InfoJson("Created", creatingUser.orEmpty()))
        infoJsons.add(InfoJson("Created Date", createdDate.orEmpty()))
        infoJsons.add(InfoJson("Modified", modifyingUser.orEmpty()))
        infoJsons.add(InfoJson("Modified Date", modifiedDate.orEmpty()))
        return infoJsons
    }

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(type)
        writeString(body)
        writeString(parentId)
        writeString(parentType)
        writeString(modifyingUser)
        writeString(creatingUser)
        writeString(createdDate)
        writeString(modifiedDate)
        writeString(formAction)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<NoteJson> = object : Parcelable.Creator<NoteJson> {
            override fun createFromParcel(source: Parcel): NoteJson = NoteJson(source)
            override fun newArray(size: Int): Array<NoteJson?> = arrayOfNulls(size)
        }
    }
}