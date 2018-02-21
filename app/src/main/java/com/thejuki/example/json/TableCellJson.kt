package com.thejuki.example.json

import android.os.Parcel
import android.os.Parcelable

/**
 * Table Cell Json
 *
 * Data class used in list view cells
 *
 * @author **TheJuki** ([GitHub](https://github.com/TheJuki))
 * @version 1.0
 */
data class TableCellJson(var id: String? = null,
                         var status: String? = null,
                         var date: String? = null,
                         var account: String? = null,
                         var category: String? = null,
                         var contact: String? = null
) : Parcelable {
    constructor(source: Parcel) : this(
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
        writeString(status)
        writeString(date)
        writeString(account)
        writeString(category)
        writeString(contact)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TableCellJson> = object : Parcelable.Creator<TableCellJson> {
            override fun createFromParcel(source: Parcel): TableCellJson = TableCellJson(source)
            override fun newArray(size: Int): Array<TableCellJson?> = arrayOfNulls(size)
        }
    }
}
