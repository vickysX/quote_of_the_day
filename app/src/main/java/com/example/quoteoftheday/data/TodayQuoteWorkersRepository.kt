package com.example.quoteoftheday.data

import androidx.lifecycle.asFlow
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.quoteoftheday.FETCH_TODAY_QUOTE_WORK_NAME
import com.example.quoteoftheday.TAG_OUTPUT
import com.example.quoteoftheday.worker.GetQuoteWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TodayQuoteWorkersRepository @Inject constructor(
    private val todayQuoteRepository: TodayQuoteRepository,
    private val workManager: WorkManager
) : WorkersRepository {

    override val outputWorkInfo: Flow<WorkInfo> =
        workManager.getWorkInfosByTagLiveData(TAG_OUTPUT).asFlow().mapNotNull {
            if (it.isNotEmpty()) {
                it.first()
            } else {
                null
            }
        }

    override suspend fun fetchAndProvideQuote(){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()

        val fetchQuote = PeriodicWorkRequestBuilder<GetQuoteWorker>(
            1, TimeUnit.DAYS,
            1, TimeUnit.HOURS
        )
            .addTag(TAG_OUTPUT)
            .setConstraints(constraints)

        workManager.enqueueUniquePeriodicWork(
            FETCH_TODAY_QUOTE_WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            fetchQuote.build()
        )
        /*outputWorkInfo.firstOrNull()?.let {
            Log.d("WorkerRepository", it.state.toString())
            Log.d("WorkerRepository", it.outputData.keyValueMap[QUOTE_KEY].toString())
        }
        return if (outputWorkInfo.first().state.isFinished) {
            outputWorkInfo.first().outputData.keyValueMap[QUOTE_KEY] as TodayQuote
        } else {
            throw Exception()
        }*/
    }

}