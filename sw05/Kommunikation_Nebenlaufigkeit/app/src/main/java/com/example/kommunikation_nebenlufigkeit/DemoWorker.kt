package com.example.kommunikation_nebenlufigkeit

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class DemoWorker(ctx: Context, private val params: WorkerParameters) : Worker(ctx, params ) {
    override fun doWork(): Result {
        return try {
            val waitingTime = params.inputData.getLong(WAITING_TIME_KEY, DEFAULT_WAITING_TIME)
            Thread.sleep(waitingTime)
            Data.Builder()
                .putLong(WAITING_TIME_KEY,MainActivity.WAITING_TIME_MILLIS)
                .build()
            Result.success() // last statement is automatically the return value
        } catch (throwable: Throwable) {
            Result.failure()
        }
    }


    companion object {
        const val WAITING_TIME_KEY: String = "WaitingTimeKey"
        const val DEFAULT_WAITING_TIME: Long = 0L
    }

}
