package com.example.crud_34a.adapter

import UpdateBookActivity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crud_34a.model.BookModel
import com.example.task_for_crud.R
import com.squareup.picasso.Picasso

class BookAdapter (var context: Context,var data :
ArrayList<BookModel>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>(){

    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var bookName : TextView = view.findViewById(R.id.lblName)
        var bookPrice : TextView = view.findViewById(R.id.lblPrice)
        var bookDesc : TextView = view.findViewById(R.id.lblDescription)
        var btnEdit : TextView = view.findViewById(R.id.btnEdit)
        var imageView : ImageView = view.findViewById(R.id.imageView45)
        var progressBar : ProgressBar = view.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        var view = LayoutInflater.from(parent.context).
        inflate(R.layout.samplebooks,
            parent,false)

        return BookViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bookName.text = data[position].bookName
        holder.bookPrice.text = data[position].bookPrice.toString()
        holder.bookDesc.text = data[position].bookDesc

        var imageUrl = data[position].url
        Picasso.get().load(imageUrl).into(holder.imageView,object: com.squareup.picasso.Callback {
            override fun onSuccess() {
                holder.progressBar.visibility = View.INVISIBLE
            }

            override fun onError(e: Exception?) {
            }

        })

        holder.btnEdit.setOnClickListener {
            var intent = Intent(context, UpdateBookActivity::class.java)
            intent.putExtra("product",data[position])
            context.startActivity(intent)
        }
    }

    fun getBookId(position: Int) : String {
        return data[position].id
    }

    fun getImageName(position: Int): String{
        return data[position].imageName
    }

    fun updateData(products: List<BookModel>){
        data.clear()
        data.addAll(products)
        notifyDataSetChanged()
    }
}