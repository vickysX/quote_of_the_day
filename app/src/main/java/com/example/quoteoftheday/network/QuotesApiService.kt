package com.example.quoteoftheday.network

import com.example.quoteoftheday.model.TodayQuote
import retrofit2.http.GET

interface QuotesApiService {

    @GET("random")
    suspend fun getQuote(): TodayQuote

}