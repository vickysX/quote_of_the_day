package com.example.quoteoftheday.data

import com.example.quoteoftheday.model.FavoriteQuote
import kotlinx.coroutines.flow.Flow

interface FavoriteQuotesRepository {
    suspend fun saveQuote(quote: FavoriteQuote)
    suspend fun deleteQuote(quote: FavoriteQuote)
    suspend fun updateQuote(quote: FavoriteQuote)
    fun getFavoriteQuotes() : Flow<List<FavoriteQuote>?>
    fun getFavoriteQuoteByTextAndAuthor(text: String, author: String) : Flow<FavoriteQuote?>
}