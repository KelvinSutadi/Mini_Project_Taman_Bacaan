package com.example.mini_project_taman_bacaan

import retrofit2.http.GET

interface BookApiService {
    @GET("/v1/8d91e733-f379-4ebd-b135-6d6e19d1d8fb")
    suspend fun getBooks(): List<Book>
}