package com.example.services_broadcastreceiver.service

interface MusicPlayerApi {
    fun playNext(): String
    fun queryHistory(): List<String>
}