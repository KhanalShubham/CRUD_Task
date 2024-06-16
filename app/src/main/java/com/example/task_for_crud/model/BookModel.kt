package com.example.crud_34a.model

import android.os.Parcel
import android.os.Parcelable
import com.example.task_for_crud.viewmodel.BookViewModel

data class BookModel(
    var id : String = "",
    var bookName : String = "",
    var bookPrice : Int = 0,
    var bookDesc : String = "",
    var url : String = "",
    var imageName : String = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(bookName)
        parcel.writeInt(bookPrice)
        parcel.writeString(bookDesc)
        parcel.writeString(url)
        parcel.writeString(imageName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookModel> {
        override fun createFromParcel(parcel: Parcel): BookModel{
            return BookModel(parcel)
        }

        override fun newArray(size: Int): Array<BookModel?> {
            return arrayOfNulls(size)
        }
    }
}