package com.example.quoteoftheday.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.quoteoftheday.model.FavoriteQuote

@Database(entities = [FavoriteQuote::class], version = 1)
@TypeConverters(Converters::class)
abstract class QuotesDatabase : RoomDatabase() {

    abstract fun quotesDao() : QuotesDao

    companion object {
        @Volatile
        private var instance : QuotesDatabase? = null

        fun getDatabase(context: Context) : QuotesDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    QuotesDatabase::class.java,
                    "quotes_database")
                    .build()
                    .also { instance = it }
            }
        }
    }

}