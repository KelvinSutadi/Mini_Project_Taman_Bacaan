package com.example.mini_project_taman_bacaan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BorrowedBookAdapter(
    private var borrowedBooks: List<Book>,
    private val onReturnClicked: (Book) -> Unit
) : RecyclerView.Adapter<BorrowedBookAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.borrowedTitleTextView)
        val author: TextView = view.findViewById(R.id.borrowedAuthorTextView)
        val returnButton: Button = view.findViewById(R.id.returnButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.borrowed_book_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = borrowedBooks[position]
        holder.title.text = book.title
        holder.author.text = book.author
        holder.returnButton.setOnClickListener {
            onReturnClicked(book)
        }
    }

    override fun getItemCount() = borrowedBooks.size

    fun updateData(newBooks: List<Book>) {
        this.borrowedBooks = newBooks
        notifyDataSetChanged()
    }
}