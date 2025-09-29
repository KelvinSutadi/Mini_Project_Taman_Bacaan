package com.example.mini_project_taman_bacaan

import androidx.lifecycle.MutableLiveData

object BorrowingManager {
    private val borrowedBooksMap = mutableMapOf<String, MutableList<Book>>()
    val borrowedBooksLiveData = MutableLiveData<Map<String, List<Book>>>()

    fun borrow(username: String, book: Book): Boolean {
        val userBorrows = borrowedBooksMap.getOrPut(username) { mutableListOf() }
        if (!userBorrows.any { it.id == book.id }) {
            userBorrows.add(book)
            borrowedBooksLiveData.value = borrowedBooksMap
            return true
        }
        return false
    }

    fun returnBook(username: String, book: Book) {
        borrowedBooksMap[username]?.removeAll { it.id == book.id }
        borrowedBooksLiveData.value = borrowedBooksMap
    }
}