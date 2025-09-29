// com/example/mini_project_taman_bacaan/Book.kt
package com.example.mini_project_taman_bacaan

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.UUID

@JsonClass(generateAdapter = true)
data class Book(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    @Json(name = "cover_url")
    val coverUrl: String,
    val author: String,
    val publisher: String,
    @Json(name = "publication_year")
    val publicationYear: Int,
    var stock: Int
)