package com.keyur43.book_tracker.network

import com.keyur43.book_tracker.data.GoogleBooksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksService {

    @GET("volumes")
    suspend fun getBooks(
        @Query("q") query: String,
    ): GoogleBooksResponse
}