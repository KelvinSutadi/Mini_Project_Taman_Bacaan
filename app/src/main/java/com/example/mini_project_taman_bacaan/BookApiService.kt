package com.example.mini_project_taman_bacaan

import retrofit2.http.GET

interface BookApiService {
    @GET("/v1/f47fba39-5a71-4229-b3bd-6adcc2234469")
    suspend fun getBooks(): List<Book>
}