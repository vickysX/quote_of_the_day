package com.example.quoteoftheday.ui.quotescreen

import androidx.annotation.DrawableRes
import com.example.quoteoftheday.model.TodayQuote

data class TodayQuoteUiState(
    val quote: String,
    val author: String,
    var isAFavorite: Boolean = false,
    @DrawableRes var imageRes: Int = PhotoSource.photos.random()
)

fun TodayQuote.toUiState() : TodayQuoteUiState {
    return TodayQuoteUiState(
        quote, author
    )
}