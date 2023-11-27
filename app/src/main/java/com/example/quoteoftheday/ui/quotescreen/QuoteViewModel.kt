package com.example.quoteoftheday.ui.quotescreen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quoteoftheday.data.FavoriteQuotesRepository
import com.example.quoteoftheday.data.WorkersRepository
import com.example.quoteoftheday.model.FavoriteQuote
import com.example.quoteoftheday.model.TodayQuote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuoteViewModel @Inject constructor(
    private val workersRepository: WorkersRepository,
    private val favoriteQuotesRepository: FavoriteQuotesRepository
) : ViewModel() {

    private lateinit var todayQuote: TodayQuote

    private var _todayQuoteUiState = MutableStateFlow(todayQuote.toUiState())
    val todayQuoteUiState : StateFlow<TodayQuoteUiState>
        get() = _todayQuoteUiState.asStateFlow()

    fun addQuoteToFavorites(
        quote: String,
        note: String = "",
        photoUri: Uri = Uri.EMPTY
    ) {
        viewModelScope.launch {
            val favoriteQuote = FavoriteQuote(
                quote = quote,
                note = note,
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

    private fun setTodayQuoteUiState() {

    }

    init {
        getTodayQuote()
    }
}