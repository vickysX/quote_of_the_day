package com.example.quoteoftheday.data

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val isQuoteFavorite: Flow<Boolean>
    suspend fun saveFavoritePreference(isFavorite: Boolean)
}