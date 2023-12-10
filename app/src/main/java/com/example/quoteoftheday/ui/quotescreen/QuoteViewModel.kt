package com.example.quoteoftheday.ui.quotescreen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoteoftheday.TAG_OUTPUT
import com.example.quoteoftheday.data.FavoriteQuotesRepository
import com.example.quoteoftheday.data.UserPreferencesRepository
import com.example.quoteoftheday.data.WorkersRepository
import com.example.quoteoftheday.model.FavoriteQuote
import com.example.quoteoftheday.model.TodayQuote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val workersRepository: WorkersRepository,
    private val favoriteQuotesRepository: FavoriteQuotesRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val userPreferencesFlow = preferencesRepository.isQuoteFavorite

    /*val todayQuoteUiState: StateFlow<TodayQuoteUiState> =
        workersRepository.outputWorkInfo.map {workInfo ->
            val quote = workInfo.outputData.keyValueMap[TAG_OUTPUT] as TodayQuote
            when {
                workInfo.state.isFinished && quote != null -> {
                    TodayQuoteUiState.FetchedQuoteComplete(
                        quote = quote.quote,
                        author = quote.author,
                    )
                }
                else -> TodayQuoteUiState.Loading
            }
        }
            .stateIn(
                scope = viewModelScope,
                initialValue = TodayQuoteUiState.Loading,
                started = SharingStarted.WhileSubscribed(5_000L)
            )*/
    val todayQuoteUiState: StateFlow<TodayQuoteUiState> =
        workersRepository.outputWorkInfo
            .combine(userPreferencesFlow) { workInfo, isFavorite ->
                val quote = workInfo.outputData.keyValueMap[TAG_OUTPUT] as TodayQuote
                when {
                    workInfo.state.isFinished && quote != null -> {
                        TodayQuoteUiState.FetchedQuoteComplete(
                            quote = quote.quote,
                            author = quote.author,
                            isAFavorite = isFavorite
                        )
                    }
                    else -> TodayQuoteUiState.Loading
                }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = TodayQuoteUiState.Loading
            )



    fun addQuoteToFavorites(
        quote: String,
        author: String = "",
        note: String = "",
        photoUri: Uri = Uri.EMPTY
    ) {
        viewModelScope.launch {
            val favoriteQuote = FavoriteQuote(
                quote = quote,
                note = note,
                author = author,
                photoUri = photoUri
            )
            favoriteQuotesRepository.saveQuote(favoriteQuote)
        }
    }

    fun removeQuoteFromFavorites(
        quote: String,
        author: String = "",
    ) {
        viewModelScope.launch {
            val quoteToRemove =
                favoriteQuotesRepository
                    .getFavoriteQuoteByTextAndAuthor(quote, author)
                    .firstOrNull()
            if (quoteToRemove != null) {
                favoriteQuotesRepository.deleteQuote(quoteToRemove)
            }
        }
    }

    fun setUserPreference(isFavorite: Boolean) {
        viewModelScope.launch {
            preferencesRepository.saveFavoritePreference(isFavorite)
        }
    }

    private fun getTodayQuote() {
        viewModelScope.launch {
            workersRepository.fetchAndProvideQuote()
        }
    }

    init {
        getTodayQuote()
    }
}