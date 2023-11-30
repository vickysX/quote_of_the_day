package com.example.quoteoftheday.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.quoteoftheday.model.FavoriteQuote
import kotlinx.coroutines.flow.Flow

@Dao
interface QuotesDao {
    @Insert
    suspend fun saveQuote(favoriteQuote: FavoriteQuote)

    @Delete
    suspend fun deleteQuote(exFavoriteQuote: FavoriteQuote)

    @Update
    suspend fun updateQuote(favoriteQuote: FavoriteQuote)

    @Query("SELECT * FROM favorite_quotes")
    fun getFavoriteQuotes() : Flow<List<FavoriteQuote>?>

    @Query("SELECT * FROM favorite_quotes WHERE quote = :text AND author = :author")
    fun getQuoteByTextAndAuthor(text: String, author: String) : Flow<FavoriteQuote?>
}