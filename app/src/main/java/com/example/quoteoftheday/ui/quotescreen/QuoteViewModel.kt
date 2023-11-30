package com.example.quoteoftheday.ui.quotescreen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoteoftheday.data.FavoriteQuotesRepository
import com.example.quoteoftheday.data.UserPreferencesRepository
import com.example.quoteoftheday.data.WorkersRepository
import com.example.quoteoftheday.model.FavoriteQuote
import com.example.quoteoftheday.model.TodayQuote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val workersRepository: WorkersRepository,
    private val favoriteQuotesRepository: FavoriteQuotesRepository,
    private val preferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private lateinit var todayQuote: TodayQuote
    private var quoteUiState = todayQuote.toUiState()
    /*private var _todayQuoteUiState = MutableStateFlow(todayQuote.toUiState())
    val todayQuoteUiState : StateFlow<TodayQuoteUiState>
        get() = _todayQuoteUiState.asStateFlow()*/

    val todayQuoteUiState: StateFlow<TodayQuoteUiState> =
        preferencesRepository.isQuoteFavorite
        .map {
            TodayQuoteUiState(
                quote = quoteUiState.quote,
                author = quoteUiState.author,
                isAFavorite = it,
            )
        }
        .stateIn(
            initialValue = quoteUiState,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L)
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

    private fun getTodayQuote() {
        viewModelScope.launch {
            // TODO: Handle exceptions
            todayQuote = workersRepository.fetchAndProvideQuote()
        }
    }

    init {
        getTodayQuote()
    }
}