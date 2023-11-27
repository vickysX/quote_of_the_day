package com.example.quoteoftheday.data

import com.example.quoteoftheday.model.TodayQuote
import com.example.quoteoftheday.network.QuotesApiService
import javax.inject.Inject

class OnlineTodayQuoteRepository @Inject constructor(
    private val apiService: QuotesApiService
) : TodayQuoteRepository {
    override suspend fun getTodayQuote(): TodayQuote = apiService.getQuote()
}