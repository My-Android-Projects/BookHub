package com.srs.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.srs.bookhub.R
import com.srs.bookhub.activity.DescriptionActivity

import com.srs.bookhub.model.Book

class DashboardRecyclerAdapter(val context: Context, val itemList:ArrayList<Book>) :RecyclerView.Adapter<DashboardRecyclerAdapter.DashBoardViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashBoardViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row,parent,false)
        return DashBoardViewHolder(view)
    }
    class DashBoardViewHolder(view:View):RecyclerView.ViewHolder(view)
    {
        val txtBookName:TextView=view.findViewById(R.id.txtBookName)
        val txtBookAuthor:TextView=view.findViewById(R.id.txtBookAuthor)
        val txtBookPrice:TextView=view.findViewById(R.id.txtBookPrice)
        val txtBookRating:TextView=view.findViewById(R.id.txtBookRating)
        val imgBookImage: ImageView =view.findViewById(R.id.imgBookImage)
        val llContent:LinearLayout=view.findViewById(R.id.llContent)


    }

    override fun onBindViewHolder(holder: DashBoardViewHolder, position: Int) {
        val book: Book =itemList[position]
        holder.txtBookName.text=book.bookName
        holder.txtBookAuthor.text=book.bookAuthor
        holder.txtBookPrice.text=book.bookPrice
        holder.txtBookRating.text=book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover).into(holder.imgBookImage)

        holder.llContent.setOnClickListener(){
         val intent= Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",book.bookId)
            context.startActivity(intent)
        }

    }



    override fun getItemCount(): Int {
       return itemList.size
    }
}