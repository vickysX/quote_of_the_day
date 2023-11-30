package com.example.quoteoftheday.data

import com.example.quoteoftheday.model.FavoriteQuote
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineFavoriteQuotesRepository @Inject constructor(
    private val quotesDao: QuotesDao
) : FavoriteQuotesRepository {

    override suspend fun saveQuote(quote: FavoriteQuote) {
        quotesDao.saveQuote(quote)
    }

    override suspend fun deleteQuote(quote: FavoriteQuote) {
        quotesDao.deleteQuote(quote)
    }

    override suspend fun updateQuote(quote: FavoriteQuote) {
        quotesDao.updateQuote(quote)
    }

    override fun getFavoriteQuotes(): Flow<List<FavoriteQuote>?> {
        return quotesDao.getFavoriteQuotes()
    }

    override fun getFavoriteQuoteByTextAndAuthor(
        text: String,
        author: String
    ): Flow<FavoriteQuote?> {
        return quotesDao.getQuoteByTextAndAuthor(text, author)
    }

}