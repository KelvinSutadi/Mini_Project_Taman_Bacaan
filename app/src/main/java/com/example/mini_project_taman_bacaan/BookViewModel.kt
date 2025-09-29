package com.example.mini_project_taman_bacaan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BookViewModel : ViewModel() {
    val books: LiveData<List<Book>> = BookManager.booksLiveData
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        fetchBooksFromApi()
    }

    private fun fetchBooksFromApi() {
        if (BookManager.booksLiveData.value.isNullOrEmpty()) {
            viewModelScope.launch {
                _isLoading.value = true
                try {
                    val bookListFromApi = Api.retrofitService.getBooks()
                    BookManager.setInitialBooks(bookListFromApi)
                    _error.value = null
                } catch (e: Exception) {
                    _error.value = "Gagal memuat data dari internet: ${e.message}"
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }
}