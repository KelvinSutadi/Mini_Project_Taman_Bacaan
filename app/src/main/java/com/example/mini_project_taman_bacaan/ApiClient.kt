package com.example.mini_project_taman_bacaan

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

// BASE_URL HANYA berisi alamat server utama
private const val BASE_URL = "https://mocki.io/v1/57001aa4-d829-44bd-b231-486047c56e35/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

object Api {
    val retrofitService: BookApiService by lazy {
        retrofit.create(BookApiService::class.java)
    }
}