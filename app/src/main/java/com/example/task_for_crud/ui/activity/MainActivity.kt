package com.example.task_for_crud.ui.activity

import BookRepositoryImpl
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crud_34a.adapter.BookAdapter
import com.example.crud_34a.ui.activity.AddBookActivity
import com.example.task_for_crud.R
import com.example.task_for_crud.databinding.ActivityMainBinding
import com.example.task_for_crud.viewmodel.BookViewModel

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding


    lateinit var bookAdapter: BookAdapter

    lateinit var bookViewModel: BookViewModel


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        bookAdapter= BookAdapter(this@MainActivity,ArrayList())

        var repo = BookRepositoryImpl()
        bookViewModel = BookViewModel(repo)

        bookViewModel.fetchAllBooks()

        bookViewModel.bookList.observe(this){
            it?.let { books ->
                bookAdapter.updateData(books)
            }
        }
        bookViewModel.loadingState.observe(this){loadingState->
            if(loadingState){
                mainBinding.progressMain.visibility = View.VISIBLE
            }else{
                mainBinding.progressMain.visibility = View.GONE
            }
        }

//        mainBinding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
//        mainBinding.recyclerView.adapter = productAdapter

        mainBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = bookAdapter
        }


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                var id = bookAdapter.getBookId(viewHolder.adapterPosition)
                var imageName = bookAdapter.getImageName(viewHolder.adapterPosition)

                bookViewModel.deleteBooks(id){
                        success,message->
                    if(success){
                        bookViewModel.deleteImage(imageName){
                                success,message->
                        }
                        Toast.makeText(applicationContext,message, Toast.LENGTH_LONG).show()
                    }else{

                        Toast.makeText(applicationContext,message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }).attachToRecyclerView(mainBinding.recyclerView)



        mainBinding.floatingActionButton.setOnClickListener {
            var intent = Intent(this@MainActivity,
                AddBookActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}