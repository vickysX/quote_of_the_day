package com.example.quoteoftheday.data

import androidx.work.WorkInfo
import com.example.quoteoftheday.model.TodayQuote
import kotlinx.coroutines.flow.Flow

interface WorkersRepository {
    val outputWorkInfo: Flow<WorkInfo>
    suspend fun fetchAndProvideQuote() : TodayQuote
}