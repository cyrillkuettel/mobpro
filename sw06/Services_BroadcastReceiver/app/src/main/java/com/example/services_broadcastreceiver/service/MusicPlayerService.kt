package com.example.services_broadcastreceiver.service

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_DEFAULT
import com.example.services_broadcastreceiver.MainActivity
import com.example.services_broadcastreceiver.R
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.atomic.AtomicBoolean

class MusicPlayerService : Service() {
    private val TAG = "MusicPlayerService"
    private val NOTIFICATION_ID = 23
    private val musicLibrary = Arrays.asList(
        "Beethoven - Für Elise",
        "Lo & Le Duc - 079 hett si gseit",
        "Britney Spears - One more time",
        "Knorkator -Liebeslied",
        "Miles DAvis - Blue"
    )
    private val random = Random()

    private val musicPlayerApi = MusicPlayerApiImpl()
    private val musicQueue: BlockingQueue<String> = LinkedBlockingQueue()
    private var playerThread: Thread? = null
    private val stopSignal = AtomicBoolean()
    private val history: MutableList<String> = Collections.synchronizedList<String>(ArrayList())
    private var notificationManager: NotificationManager? = null

    override fun onCreate() {
        super.onCreate()
        notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startPlayer()
        return START_STICKY
    }

    private fun startPlayer() {
        playerThread?.let { it ->
             if (it.isAlive) {
                return
            }
        }
        stopSignal.set(false)
        startPlayerThread()
        startForeground(NOTIFICATION_ID, createNotification("--waiting --"))
    }


    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
    }


    fun queryHistory(): List<String> {
        Log.i("MusicPLayerService", "Returning history with" + history.size + "entries.")
        return Collections.unmodifiableList(history)
    }

    private fun startPlayerThread() {
        playerThread = object : Thread("Player") {
            override fun run() {
                while (!stopSignal.get()) {
                    try {
                        val music: String = musicQueue.take()
                        history.add(music)
                        notificationManager?.notify(
                            NOTIFICATION_ID,
                            createNotification("Playin <$music>")
                        )
                    } catch (e: InterruptedException) {

                    }
                }
                Log.i(TAG, "Music player stopped")
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return musicPlayerApi
    }


    private fun createNotification(musicTitle: String) : Notification? {
        return NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
            .setOngoing(true)
            .setContentTitle("HSLU Music Player")
            .setTicker("HSLU Music Player")
            .setContentText(musicTitle)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setWhen(System.currentTimeMillis())
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
    }


    inner class MusicPlayerApiImpl : Binder(), MusicPlayerApi {
        override fun playNext(): String {
            return this@MusicPlayerService.playNext()
        }

        override fun queryHistory(): List<String> {

            return this@MusicPlayerService.queryHistory()
        }
    }

    private fun playNext(): String {
        val next: String = musicLibrary[random.nextInt(musicLibrary.size)]
        musicQueue.add(next)
        history.add(next)
        Log.i(TAG, "Scheduling next music: $next")
        playerThread?.let { it ->
            {
                if (it.isAlive) {
                    it.interrupt()
                }
            }
        }
        return next
    }
}

