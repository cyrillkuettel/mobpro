package com.example.ui_demo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(R.layout.activity_main) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainView_replaceFrame, MainFragment())
            .addToBackStack("Main")
            .commit()
    }

    fun layoutLinearSelected(view: View) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainView_replaceFrame, LinearLayoutFragment())
            .addToBackStack("Linear Layout Fragment")
            .commit()

    }

    fun layoutConstraintSelected(view: View) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.mainView_replaceFrame, ConstraintLayoutFragment())
            .addToBackStack("Constraint Layout Fragment")
            .commit()
    }


}