package com.example.services_broadcastreceiver.receiver

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.text.format.DateFormat
import androidx.core.app.NotificationCompat
import com.example.services_broadcastreceiver.MainActivity
import com.example.services_broadcastreceiver.MainActivity.Companion.CHANNEL_ID
import com.example.services_broadcastreceiver.R
import java.util.*

class GlobalBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        if (!TextUtils.equals(intent?.action, ACTION_MY_BROADCAST)) {
            return
        }
        val now = Date()
        broadcastTimes.add(now)
        val number = broadcastTimes.size
        val title = "Huch! Ein Broadcast!"
        val text = "Anzahl erhaltene Boadcasts: " + number + ", " +
                "letzter: " + DateFormat.format("kk:mm:ss", now)
        val pendingIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val icon = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setLargeIcon(icon)
            .setTicker(title)
            .setWhen(now.time)
            .setContentIntent(pendingIntent)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)


    }


    companion object {
        var broadcastTimes: MutableList<Date> = ArrayList()
        val ACTION_MY_BROADCAST = "ACTION_MY_BROADCAST"
        val NOTIFICATION_ID = 22
    }

}