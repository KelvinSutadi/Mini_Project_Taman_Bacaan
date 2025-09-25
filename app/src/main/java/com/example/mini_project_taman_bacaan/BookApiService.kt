package com.example.mini_project_taman_bacaan

import retrofit2.http.GET
import com.example.mini_project_taman_bacaan.Book

interface BookApiService {
    // @GET HANYA berisi path unik dari URL Mocki Anda
    @GET("/v1/57001aa4-d829-44bd-b231-486047c56e35")
    suspend fun getBooks(): List<Book>
}