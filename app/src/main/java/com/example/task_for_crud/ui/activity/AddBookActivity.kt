package com.example.crud_34a.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crud_34a.R
import com.example.crud_34a.databinding.ActivityAddProductBinding
import com.example.crud_34a.model.ProductModel
import com.example.crud_34a.repository.ProductRepositoryImpl
import com.example.crud_34a.utils.ImageUtils
import com.example.crud_34a.viewmodel.ProductViewModel
import com.example.task_for_crud.databinding.ActivityAddBookBinding
import com.example.task_for_crud.model.BookModel
import com.example.task_for_crud.repository.BookRepositoryImpl
import com.example.task_for_crud.viewmodel.BookViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.UUID

class AddBookActivity : AppCompatActivity() {
    lateinit var addBookBinding: ActivityAddBookBinding


    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var imageUri: Uri? = null

    lateinit var imageUtils: ImageUtils

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

        imageUtils = ImageUtils(this)
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
                    addProduct(imageUrl,imageName)
                }else{
                    Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun addProduct(url: String?, imageName: String?) {
        var productName: String = addBookBinding.editTextName.text.toString()
        var desc: String = addBookBinding.editTextDesc.text.toString()
        var price: Int = addBookBinding.editTextPrice.text.toString().toInt()

        var data = BookModel("",productName,price,desc,
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