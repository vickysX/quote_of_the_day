package com.example.quoteoftheday.modules

import com.example.quoteoftheday.data.FavoriteQuotesRepository
import com.example.quoteoftheday.data.OfflineFavoriteQuotesRepository
import com.example.quoteoftheday.data.OnlineTodayQuoteRepository
import com.example.quoteoftheday.data.PreferencesRepository
import com.example.quoteoftheday.data.TodayQuoteRepository
import com.example.quoteoftheday.data.TodayQuoteWorkersRepository
import com.example.quoteoftheday.data.UserPreferencesRepository
import com.example.quoteoftheday.data.WorkersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

    @Binds
    abstract fun provideOnlineRepository(
        onlineTodayQuoteRepository: OnlineTodayQuoteRepository
    ) : TodayQuoteRepository

    @Binds
    abstract fun provideFavoriteQuoteRepository(
        offlineFavoriteQuotesRepository: OfflineFavoriteQuotesRepository
    ) : FavoriteQuotesRepository

    @Binds
    abstract fun provideWorkersRepository(
        todayQuoteWorkersRepository: TodayQuoteWorkersRepository
    ) : WorkersRepository

    @Binds
    abstract fun providePreferencesRepository(
        userPreferencesRepository: UserPreferencesRepository
    ) : PreferencesRepository

}