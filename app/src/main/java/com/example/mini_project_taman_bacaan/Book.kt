package com.example.mini_project_taman_bacaan

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Book(
    val title: String,
    val description: String,
    @Json(name = "cover_url")
    val coverUrl: String,
    val author: String,
    val publisher: String,
    @Json(name = "publication_year")
    val publicationYear: Int
)