package com.example.mini_project_taman_bacaan

import retrofit2.http.GET

interface BookApiService {
    @GET("v1/57001aa4-d829-44bd-b231-486047c56e35")
    suspend fun getBooks(): List<Book>
}