package com.example.task_for_crud.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crud_34a.model.BookModel
import com.example.crud_34a.repository.BookRepository

class BookViewModel (val repository: BookRepository) : ViewModel(){

    fun deleteBooks(id: String,callback: (Boolean, String?) -> Unit){
        repository.deleteBooks(id,callback)
    }
    fun deleteImage(imageName: String,callback: (Boolean, String?) -> Unit){
        repository.deleteImage(imageName,callback)
    }
    fun updateBooks(id:String,data: MutableMap<String,Any>?,
                       callback: (Boolean, String?) -> Unit){
        repository.updateBooks(id,data,callback)
    }

    fun uploadImages(imageName:String, imageUri: Uri, callback: (Boolean, String?, String?) -> Unit) {
        repository.uploadImages(imageName,imageUri) { success, imageUrl,message ->
            callback(success, imageUrl,message)
        }
    }
    fun addBooks(bookModel: BookModel, callback: (Boolean, String?) -> Unit){
        repository.addBooks(bookModel,callback)
    }

    var _bookList = MutableLiveData<List<BookModel>?>()

    var bookList = MutableLiveData<List<BookModel>?>()
        get() = _bookList

    var _loadingState = MutableLiveData<Boolean>()
    var loadingState = MutableLiveData<Boolean>()
        get() = _loadingState

    fun fetchAllBooks(){
        _loadingState.value = true
        repository.getAllBooks { books, success,message ->
            if(books!=null){
                _loadingState.value = false
                _bookList.value = books
            }
        }
    }
}