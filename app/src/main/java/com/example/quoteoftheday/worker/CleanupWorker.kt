package com.example.quoteoftheday.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.quoteoftheday.CACHE_FILE_NAME
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

@HiltWorker
class CleanupWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return withContext(dispatcher) {
            return@withContext try {
                val cacheFile = File(applicationContext.cacheDir, CACHE_FILE_NAME)
                if (cacheFile.exists()) {
                    cacheFile.delete()
                }
                Result.success()
            } catch (exception: Exception) {
                Result.failure()
            }
        }
    }

}