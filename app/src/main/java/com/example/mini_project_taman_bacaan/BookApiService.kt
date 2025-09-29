package com.example.mini_project_taman_bacaan

import retrofit2.http.GET

interface BookApiService {
    @GET("/v1/2133c2b4-129a-4e5f-bf78-0f9fc33fb951")
    suspend fun getBooks(): List<Book>
}