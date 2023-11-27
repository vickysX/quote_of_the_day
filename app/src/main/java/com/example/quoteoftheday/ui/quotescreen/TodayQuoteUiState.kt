package com.example.quoteoftheday.ui.quotescreen

import androidx.annotation.DrawableRes
import com.example.quoteoftheday.model.TodayQuote

data class TodayQuoteUiState(
    val quote: String,
    val author: String,
    @DrawableRes var iconRes: Int = 0
)

fun TodayQuote.toUiState() : TodayQuoteUiState {
    return TodayQuoteUiState(
        quote, author
    )
}