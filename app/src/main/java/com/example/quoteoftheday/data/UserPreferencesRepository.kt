package com.example.quoteoftheday.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferencesRepository {

    override val isQuoteFavorite: Flow<Boolean> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    Log.e(TAG, "Error reading preferences", it)
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }
            .map { preferences ->
            preferences[IS_QUOTE_FAVORITE] ?: false
        }

    override suspend fun saveFavoritePreference(isFavorite: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_QUOTE_FAVORITE] = isFavorite
        }
    }

    private companion object {
        val IS_QUOTE_FAVORITE = booleanPreferencesKey("is_quote_favorite")
        const val TAG = "UserPreferencesRepo"
    }

}