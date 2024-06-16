import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crud_34a.model.BookModel
import com.example.task_for_crud.R
import com.example.task_for_crud.databinding.ActivityAddBookBinding
import com.example.task_for_crud.databinding.ActivityUpdateBookBinding
import com.example.task_for_crud.utils.Imageutils
import com.example.task_for_crud.viewmodel.BookViewModel
import com.squareup.picasso.Picasso


class UpdateBookActivity : AppCompatActivity() {
    lateinit var updateBookBinding: ActivityUpdateBookBinding
    var id = ""
    var imageName = ""
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
        updateBookBinding=ActivityUpdateBookBinding.inflate(layoutInflater)
        setContentView(updateBookBinding.root)

        imageUtils = Imageutils(this)

        var repo = BookRepositoryImpl()
        bookViewModel = BookViewModel(repo)

        imageUtils.registerActivity {url->
            imageUri = url
            Picasso.get().load(imageUri).into(updateBookBinding.imageUpdate)

        }


        var book : BookModel? = intent.getParcelableExtra("book")

        updateBookBinding.editTextNameUpdate.setText(book?.bookName)
        updateBookBinding.editTextPriceUpdate.setText(book?.bookPrice.toString())
        updateBookBinding.editTextDescUpdate.setText(book?.bookDesc)

        Picasso.get().load(book?.url).into(updateBookBinding.imageUpdate)


        id = book?.id.toString()
        imageName = book?.imageName.toString()

        updateBookBinding.buttonUpdate.setOnClickListener {
            uploadImage()
        }

        updateBookBinding.imageUpdate.setOnClickListener {
            imageUtils.launchGallery(this)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun uploadImage(){

        imageUri?.let {
            bookViewModel.uploadImages(imageName,it) { success, imageUrl, message ->
                if(success){
                    updateBook(imageUrl.toString())
                }else{
                    Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun updateBook(url : String){
        var updatedName : String= updateBookBinding.editTextNameUpdate.text.toString()
        var updatedPrice: Int = updateBookBinding.editTextPriceUpdate.text.toString().toInt()
        var updatedDesc : String = updateBookBinding.editTextDescUpdate.text.toString()

        var updatedMap = mutableMapOf<String,Any>()
        updatedMap["bookName"] = updatedName
        updatedMap["bookPrice"] = updatedPrice
        updatedMap["bookDesc"] = updatedDesc
        updatedMap["id"] = id
        updatedMap["url"] = url

        bookViewModel.updateBooks(id,updatedMap){
                success, message ->
            if(success){
                Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext,message,Toast.LENGTH_SHORT).show()

            }
        }


    }
    fun registerActivityForResult() {
        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                val resultcode = result.resultCode
                val imageData = result.data
                if (resultcode == RESULT_OK && imageData != null) {
                    imageUri = imageData.data
                    imageUri?.let {
                        Picasso.get().load(it).into(updateBookBinding.imageUpdate)
                    }
                }

            })
    }
}