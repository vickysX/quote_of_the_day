package com.example.quoteoftheday.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.quoteoftheday.QUOTE_KEY
import com.example.quoteoftheday.R
import com.example.quoteoftheday.data.TodayQuoteRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class GetQuoteWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val quoteRepository: TodayQuoteRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return withContext(dispatcher) {
            return@withContext try {
                // call online repository
                // call should be comprehensive of caching
                val todayQuote = quoteRepository.getTodayQuote()
                makeNotification(todayQuote.quote, applicationContext)
                val output = workDataOf(QUOTE_KEY to todayQuote)
                Result.success(output)
            } catch (exception: Exception) {
                val errorMessage = applicationContext.resources.getString(
                    R.string.error_message
                )
                makeNotification(errorMessage, applicationContext)
                Result.failure()
            }
        }
    }

}