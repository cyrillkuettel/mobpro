package com.example.persistenz

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.persistenz.filesystem.OverviewFragment


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.mainView_replaceFrame, OverviewFragment())
            .addToBackStack("Main")
            .commit()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }




}
