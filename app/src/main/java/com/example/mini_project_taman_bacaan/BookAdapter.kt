package com.example.mini_project_taman_bacaan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BookAdapter(
    private var books: List<Book>,
    // DITAMBAHKAN: Sebuah "listener" untuk menangani klik
    private val onBookClicked: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coverImageView: ImageView = itemView.findViewById(R.id.itemCoverImageView)
        val titleTextView: TextView = itemView.findViewById(R.id.itemTitleTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.itemAuthorTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_list_item, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.titleTextView.text = book.title
        holder.authorTextView.text = book.author

        Glide.with(holder.itemView.context)
            .load(book.coverUrl)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image)
            .into(holder.coverImageView)

        // DITAMBAHKAN: Membuat seluruh item bisa diklik
        holder.itemView.setOnClickListener {
            onBookClicked(book)
        }
    }

    override fun getItemCount(): Int {
        return books.size
    }
}