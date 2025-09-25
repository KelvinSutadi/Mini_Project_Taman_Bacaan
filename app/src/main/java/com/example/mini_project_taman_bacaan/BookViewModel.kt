package com.example.mini_project_taman_bacaan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BookViewModel : ViewModel() {

    // Mengubah LiveData untuk menampung DAFTAR buku
    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> = _books

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        fetchBookData()
    }

    private fun fetchBookData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Mengambil seluruh daftar buku dari API
                _books.value = Api.retrofitService.getBooks()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Gagal memuat data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}