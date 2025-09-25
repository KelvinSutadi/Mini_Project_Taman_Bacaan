package com.example.mini_project_taman_bacaan

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel yang disederhanakan.
 * Tugasnya hanya menyediakan akses ke LiveData dari BookManager.
 */
class BookViewModel : ViewModel() {
    // Langsung mengambil LiveData dari BookManager
    val books: LiveData<List<Book>> = BookManager.booksLiveData
}