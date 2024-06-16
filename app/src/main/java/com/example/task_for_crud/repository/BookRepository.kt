package com.example.crud_34a.repository

import android.net.Uri
import com.example.crud_34a.model.BookModel


interface BookRepository {
    fun addBooks(BookModel: BookModel,callback:(Boolean,String?)-> Unit)
    fun uploadImages(imageName: String, imageUri : Uri,
                     callback:(Boolean,String?,String?)-> Unit)

    fun getAllBooks(callback: (List<BookModel>?, Boolean,
                                  String?) -> Unit)
    fun updateBooks(id:String,data: MutableMap<String,Any>?,
                       callback: (Boolean, String?) -> Unit)

    fun deleteBooks(id: String,callback: (Boolean, String?) -> Unit)
    fun deleteImage(imageName: String,callback: (Boolean, String?) -> Unit)
}

