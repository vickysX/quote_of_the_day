package com.example.quoteoftheday.modules

import android.content.Context
import com.example.quoteoftheday.data.QuotesDao
import com.example.quoteoftheday.data.QuotesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideDatabase(@ApplicationContext context: Context) : QuotesDatabase {
        return QuotesDatabase.getDatabase(context)
    }

    @Provides
    fun provideDao(quotesDatabase: QuotesDatabase) : QuotesDao {
        return quotesDatabase.quotesDao()
    }

}