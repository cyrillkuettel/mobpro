package com.example.persistenz.filesystem

import android.app.Application
import android.os.Environment
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.*

class FileReaderViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "FileReaderViewModel"


    // observable data holder class. LiveData is lifecycle-aware
    private var storageStateText: MutableLiveData<String> = MutableLiveData()
    private var storedText: MutableLiveData<String> = MutableLiveData()
    private var isExternalStorageEnabled: MutableLiveData<Boolean> = MutableLiveData()
    private val context = application

    fun getStoredText():LiveData<String> {
        return storedText
    }

    fun getStorageStateText(): LiveData<String> {
        return storageStateText
    }

    fun isExternalStorageEnabled() : LiveData<Boolean> {
        return isExternalStorageEnabled
    }




    fun writeTextToFile(text: String, useExternalStorage: Boolean)  {
        Log.v(TAG, "Writing File");
        var writer: Writer? = null
        try {
            writer = BufferedWriter(FileWriter(getFile(useExternalStorage)))
            writer.write(text)
            storedText.value = "<ok>"
        } catch (ex: IOException) {
            Log.e(TAG, "Could not write file", ex)
            storedText.value = "<write failed>"
        } finally {
            try {
                writer?.close()
            } catch (e: Exception) {
                Log.e(TAG, "Could not close file", e)
            }
        }
    }

    fun readTextFromFile(useExternalStorage: Boolean)  {
        Log.v(TAG, "Reading File");

        val inFile = getFile(useExternalStorage)
        if (!inFile.exists()) {
            storedText.value = "<no file"
            return
        }
        var reader: Reader? = null

        try {
            reader = BufferedReader(FileReader(inFile))
            val stringBuilder = StringBuilder()
            var line: String?
            while (reader.readLine().also {line = it } != null) {
                stringBuilder.append(line)
                stringBuilder.append("\n")
            }
            storedText.value = stringBuilder.toString()
        } catch (ex: IOException) {
            Log.e(TAG, "Could not read file", ex)
            storedText.value = "<reading failed>"
        } finally {
            try {
                reader?.close()
            } catch (e: Exception) {
                Log.e(TAG, "Could not close Reader", e)

            }
        }
    }

    private fun getFile(useExStorage: Boolean): File {
        val rootDir: File? = if(useExStorage) {
            context.getExternalFilesDir(null)
        } else {
            getApplication<Application>().applicationContext.filesDir
        }
        val parentDir = File(rootDir, "HSLU-MobPro-3")
        parentDir.mkdirs()
        return File(parentDir, "PersistentMessage.txt")

    }

    fun updateStorageState() {
        val state = Environment.getExternalStorageState()
        when {
            Environment.MEDIA_MOUNTED == state -> {
                storageStateText.value = "External storage is mounted"

            }
            Environment.MEDIA_MOUNTED_READ_ONLY == state -> {
                storageStateText.value = "External storage is mounted, but read-only"

                // if read-only, then it should also be false, no?
            }
            else -> {
                storageStateText.value = "External Storage is not available ($state)"
                isExternalStorageEnabled.value = false
            }
        }
    }



}