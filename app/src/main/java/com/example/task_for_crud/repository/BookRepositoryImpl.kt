import android.net.Uri
import com.example.crud_34a.model.BookModel
import com.example.crud_34a.repository.BookRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class BookRepositoryImpl : BookRepository {
    var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    var ref: DatabaseReference = firebaseDatabase.reference.child("books")

    var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    var storageReference: StorageReference = firebaseStorage.reference.child("books")
    override fun addBooks(bookModel: BookModel, callback: (Boolean, String?) -> Unit) {
        var id = ref.push().key.toString()
        bookModel.id = id

        ref.child(id).setValue(bookModel).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Book added successfully")
            } else {
                callback(false, "Unable to add books")
            }
        }
    }

    override fun uploadImages(
        imageName: String,
        imageUri: Uri,
        callback: (Boolean, String?, String?) -> Unit
    ) {
//
        var imageReference = storageReference.child("books").child(imageName)
        imageUri.let { url ->
            imageReference.putFile(url).addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener { url ->
                    var imageUrl = url.toString()
                    callback(true, imageUrl, "Upload success")
                }
            }.addOnFailureListener {
                callback(false, "", "Failed to load image")
            }
        }

    }



    override fun getAllBooks(callback: (List<BookModel>?, Boolean, String?) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var bookList = mutableListOf<BookModel>()
                for (eachData in snapshot.children) {
                    var books = eachData.getValue(BookModel::class.java)
                    if (books != null) {
                        bookList.add(books)
                    }
                }
                callback(bookList, true, "Book fetched successfully")
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null, false, "Unable to fetch ${error.message}")
            }
        })
    }

    override fun updateBooks(
        id: String,
        data: MutableMap<String, Any>?,
        callback: (Boolean, String?) -> Unit
    ) {
        data?.let {
            ref.child(id).updateChildren(it).addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Data has been updated")
                } else {
                    callback(false, "Unable to upload data")
                }
            }
        }
    }

    override fun deleteBooks(id: String, callback: (Boolean, String?) -> Unit) {
        ref.child(id).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Book deleted")
            } else {
                callback(false, "unable to delete book")
            }
        }
    }

    override fun deleteImage(imageName: String, callback: (Boolean, String?) -> Unit) {

        storageReference.child("books").child(imageName).delete().addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Image deleted")
            } else {
                callback(false, "unable to delete image")
            }
        }

    }

}