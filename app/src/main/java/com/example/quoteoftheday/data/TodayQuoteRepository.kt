package com.example.quoteoftheday.data

import com.example.quoteoftheday.model.TodayQuote

interface TodayQuoteRepository {
    suspend fun getTodayQuote(): TodayQuote
}