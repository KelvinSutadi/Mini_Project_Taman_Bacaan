package com.example.mini_project_taman_bacaan

import androidx.lifecycle.MutableLiveData

// Singleton object untuk mengelola data buku (simulasi database)
object BookManager {

    // Menggunakan LiveData agar perubahan bisa diamati
    val booksLiveData = MutableLiveData<List<Book>>()

    private val initialBooks = listOf(
        Book(
            "Laskar Pelangi",
            "Kisah inspiratif...",
            "https://cdn.gramedia.com/uploads/items/9789793062792_New-Edition-Laskar-Pelangi.jpg",
            "Andrea Hirata",
            "Bentang Pustaka",
            2005
        ),
        Book(
            "Bumi Manusia",
            "Sebuah roman...",
            "http://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1464891625i/1398034._UY1600_SS1600_.jpg",
            "Pramoedya Ananta Toer",
            "Hasta Mitra",
            1980
        )
    )

    init {
        // Inisialisasi daftar buku saat aplikasi pertama kali berjalan
        booksLiveData.value = initialBooks
    }

    fun addBook(book: Book) {
        val currentList = booksLiveData.value?.toMutableList() ?: mutableListOf()
        currentList.add(book)
        booksLiveData.value = currentList
    }
}