package com.thejuki.example.json

import android.os.Parcel
import android.os.Parcelable
import com.thejuki.example.enum.ObjectType

/**
 * Contact Json
 *
 * Data class used for Contact details and form
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
data class ContactJson(var id: String? = null,
                       var firstName: String? = null,
                       var lastName: String? = null,
                       var mobilePhone: String? = null,
                       var emailAddress: String? = null,
                       var businessPhone: String? = null,
                       var supervisorName: String? = null,
                       var supervisorId: String? = null,
                       var formAction: String? = null,
                       var modifyingUser: String? = null,
                       var creatingUser: String? = null,
                       var createdDate: String? = null,
                       var modifiedDate: String? = null,
                       var teamId: String? = null,
                       var teamName: String? = null
) : Parcelable {
    fun getInfos(): ArrayList<InfoJson> {
        val infoJsons = ArrayList<InfoJson>()
        infoJsons.add(InfoJson("Number", id.orEmpty()))
        infoJsons.add(InfoJson("Name", firstName.orEmpty() + " " + lastName.orEmpty()))
        infoJsons.add(InfoJson("Team", teamName.orEmpty()))
        infoJsons.add(InfoJson("Email Address", emailAddress.orEmpty(), emailAddress.orEmpty(), ObjectType.Email))
        infoJsons.add(InfoJson("Mobile Phone", mobilePhone.orEmpty(), mobilePhone.orEmpty(), ObjectType.Phone))
        infoJsons.add(InfoJson("Business Phone", businessPhone.orEmpty(), businessPhone.orEmpty(), ObjectType.Phone))
        infoJsons.add(InfoJson("Supervisor", supervisorName.orEmpty(), supervisorId.orEmpty(), ObjectType.Contact))
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
        writeString(firstName)
        writeString(lastName)
        writeString(mobilePhone)
        writeString(emailAddress)
        writeString(businessPhone)
        writeString(supervisorName)
        writeString(supervisorId)
        writeString(formAction)
        writeString(modifyingUser)
        writeString(creatingUser)
        writeString(createdDate)
        writeString(modifiedDate)
        writeString(teamId)
        writeString(teamName)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ContactJson> = object : Parcelable.Creator<ContactJson> {
            override fun createFromParcel(source: Parcel): ContactJson = ContactJson(source)
            override fun newArray(size: Int): Array<ContactJson?> = arrayOfNulls(size)
        }
    }
}
