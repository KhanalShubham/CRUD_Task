package com.example.crud_34a.ui.activity

import BookRepositoryImpl
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crud_34a.model.BookModel
import com.example.task_for_crud.R
import com.example.task_for_crud.databinding.ActivityAddBookBinding
import com.example.task_for_crud.utils.Imageutils
import com.example.task_for_crud.viewmodel.BookViewModel
import com.squareup.picasso.Picasso
import java.util.UUID

class AddBookActivity : AppCompatActivity() {
    lateinit var addBookBinding: ActivityAddBookBinding


    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var imageUri: Uri? = null

    lateinit var imageUtils: Imageutils

    lateinit var bookViewModel: BookViewModel

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            activityResultLauncher.launch(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        addBookBinding=ActivityAddBookBinding.inflate(layoutInflater)
        setContentView(addBookBinding.root)

        imageUtils = Imageutils(this)
        imageUtils.registerActivity { url ->
            url.let {
                imageUri = it
                Picasso.get().load(it).into(addBookBinding.imageBrowse)
            }

        }

        var repo = BookRepositoryImpl()
        bookViewModel = BookViewModel(repo)


        addBookBinding.imageBrowse.setOnClickListener {
            imageUtils.launchGallery(this)
        }

        addBookBinding.button.setOnClickListener {
            if (imageUri != null) {

                uploadImage()
            } else {
                Toast.makeText(
                    applicationContext, "Please upload image first",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    fun uploadImage() {
        var imageName = UUID.randomUUID().toString()
        imageUri?.let {
            bookViewModel.uploadImages(imageName,it) { success, imageUrl,message ->
                if(success){
                    addBook(imageUrl,imageName)
                }else{
                    Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun addBook(url: String?, imageName: String?) {
        var bookName: String = addBookBinding.editTextName.text.toString()
        var desc: String = addBookBinding.editTextDesc.text.toString()
        var price: Int = addBookBinding.editTextPrice.text.toString().toInt()

        var data = BookModel("",bookName,price,desc,
            url.toString(),imageName.toString())

        bookViewModel.addBooks(data){
                success, message ->
            if(success){
                Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
                finish()
            }else{
                Toast.makeText(applicationContext,message,Toast.LENGTH_LONG).show()
            }
        }

    }


}