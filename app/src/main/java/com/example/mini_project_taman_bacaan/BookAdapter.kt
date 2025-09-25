package com.example.mini_project_taman_bacaan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BookAdapter(private var books: List<Book>) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    // ViewHolder memegang referensi ke view di dalam setiap item
    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coverImageView: ImageView = itemView.findViewById(R.id.itemCoverImageView)
        val titleTextView: TextView = itemView.findViewById(R.id.itemTitleTextView)
        val authorTextView: TextView = itemView.findViewById(R.id.itemAuthorTextView)
    }

    // Membuat ViewHolder baru (dipanggil oleh RecyclerView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_list_item, parent, false)
        return BookViewHolder(view)
    }

    // Menghubungkan data ke ViewHolder (mengisi data ke dalam item)
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.titleTextView.text = book.title
        holder.authorTextView.text = book.author

        Glide.with(holder.itemView.context)
            .load(book.coverUrl)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image)
            .into(holder.coverImageView)
    }

    // Mengembalikan jumlah total item dalam daftar
    override fun getItemCount(): Int {
        return books.size
    }
}