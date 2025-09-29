package com.example.mini_project_taman_bacaan

import androidx.lifecycle.MutableLiveData

object BookManager {
    val booksLiveData = MutableLiveData<List<Book>>(emptyList())

    fun setInitialBooks(books: List<Book>) {
        booksLiveData.value = books
    }

    fun addBook(book: Book) {
        val currentList = booksLiveData.value?.toMutableList() ?: mutableListOf()
        currentList.add(book)
        booksLiveData.value = currentList
    }

    fun borrowBook(bookId: String) {
        val currentList = booksLiveData.value?.toMutableList() ?: return
        val book = currentList.find { it.id == bookId }
        book?.let {
            if (it.stock > 0) {
                it.stock--
                booksLiveData.value = currentList
            }
        }
    }

    fun returnBook(bookId: String) {
        val currentList = booksLiveData.value?.toMutableList() ?: return
        val book = currentList.find { it.id == bookId }
        book?.let {
            it.stock++
            booksLiveData.value = currentList
        }
    }
}