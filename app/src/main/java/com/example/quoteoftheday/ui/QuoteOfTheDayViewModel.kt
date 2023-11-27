package com.example.quoteoftheday.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class QuoteOfTheDayViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(QuotesUiState())
    val uiState: StateFlow<QuotesUiState> = _uiState.asStateFlow()

    fun setTab(tab: QuotesTabs) {
        _uiState.update { current ->
            current.copy(
                currentTab = tab
            )
        }
    }
}