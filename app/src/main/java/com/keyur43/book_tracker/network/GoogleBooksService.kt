package com.app.booktracker.network

import com.app.booktracker.data.GoogleBooksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksService {

    @GET("volumes")
    suspend fun getBooks(
        @Query("q") query: String,
    ): GoogleBooksResponse
}