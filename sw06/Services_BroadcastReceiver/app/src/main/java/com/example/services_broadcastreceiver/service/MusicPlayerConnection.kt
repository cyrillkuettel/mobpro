package com.example.services_broadcastreceiver.service

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder

class MusicPlayerConnection : ServiceConnection {

    private var musicPlayerApi: MusicPlayerApi? = null

    fun getMusicPlayerApi(): MusicPlayerApi? {
        return musicPlayerApi
    }
    override fun onServiceDisconnected(name: ComponentName?) {
        musicPlayerApi = null
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        musicPlayerApi = service as MusicPlayerApi
    }
}