package com.example.services_broadcastreceiver.worker

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.lang.Exception
import java.util.*

class LocalizeMissilesWorker(private val context: Context, params: WorkerParameters): Worker(context, params) {

    private val random = Random()
    private val TAG = "LocalizeMissilesWorker"
    override fun doWork(): Result { // ListenableWorker.Result
        Log.i(TAG, "Starting doWork()")
        try {
            Thread.sleep(3000)
            if (random.nextInt(3) < 2) { // 2 out of 3
                val nofMissiles = random.nextInt(5)
                val positions = ArrayList<String>(nofMissiles)
                for (m in 0 until nofMissiles) {
                    positions.add(
                        String.format(
                            "(%02d.%03d %s, %03d.%03d %s)",
                            random.nextInt(90),
                            random.nextInt(1000),
                            if (random.nextBoolean()) "N" else "S",
                            random.nextInt(90),
                            random.nextInt(1000),
                            if (random.nextBoolean()) "W" else "E"
                        )
                    )
                }
                Log.i(TAG, nofMissiles.toString() + "found [success]")

                val data: Array<String> = positions.toTypedArray()

                val output: Data = workDataOf(Pair("missilePositions" , data))
                return Result.success(output)
            }

        } catch (e: InterruptedException) {
            Log.i(TAG, " [Interrupted]")
            return Result.retry()
        }
        Log.i(TAG, " [failed]")

        return Result.failure()

    }
}