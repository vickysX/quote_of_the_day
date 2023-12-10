package com.example.quoteoftheday.ui.quotescreen

import androidx.annotation.DrawableRes
import com.example.quoteoftheday.model.TodayQuote

sealed interface TodayQuoteUiState {
    data object Loading: TodayQuoteUiState
    data object Error: TodayQuoteUiState
    data class FetchedQuoteComplete(
        val quote: String = "",
        val author: String = "",
        var isAFavorite: Boolean = false,
        @DrawableRes var imageRes: Int = PhotoSource.photos.random()
    ) : TodayQuoteUiState
}



fun TodayQuote.toUiState() : TodayQuoteUiState.FetchedQuoteComplete {
    return TodayQuoteUiState.FetchedQuoteComplete(
        quote, author
    )
}