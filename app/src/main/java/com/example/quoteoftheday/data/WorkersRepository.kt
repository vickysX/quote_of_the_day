package com.example.quoteoftheday.data

import androidx.work.WorkInfo
import kotlinx.coroutines.flow.Flow

interface WorkersRepository {
    val outputWorkInfo: Flow<WorkInfo>
    suspend fun fetchAndProvideQuote()
}