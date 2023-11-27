package com.example.quoteoftheday.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodayQuote(
    @SerialName("q")
    val quote: String,

    @SerialName("a")
    val author: String,
)
