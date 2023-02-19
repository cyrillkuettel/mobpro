package com.example.ui_demo

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import java.lang.NullPointerException
import java.util.*


class MainFragment : DialogFragment(R.layout.fragment_main) {
    private val TAG = "MainFragment"

    // Avoid selection triggering of Spinner during initialization
    private var firstTimeSpinnerSelected = true;


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRatingBar(view)
        setupAlertdialog(view, savedInstanceState)
        setupSpinner(view)

        val mainButtonCustomIntent = view.findViewById<AppCompatButton>(R.id.mainButtonIntentWithCustom)
        mainButtonCustomIntent.setOnClickListener {startCustomIntentOnClick()  }

    }

    private fun startCustomIntentOnClick() {
        val customIntent = Intent()
        customIntent.action = MY_ACTION_SHOW_TEXT
        customIntent.addCategory(Intent.CATEGORY_DEFAULT)
        customIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val myText = """Activity gestartet durch folgende Intent-ACTION:'$MY_ACTION_SHOW_TEXT'
            Jetzt = ${Date()}""".trimIndent()

        customIntent.putExtra(MY_EXTRA_KEY, myText)
        startActivity(customIntent)
    }




    private fun setupRatingBar(view: View,) {
        val ratingBar = view.findViewById<RatingBar>(R.id.main_ratingBar)
        val ratingBarTextView = view.findViewById<TextView>(R.id.main_ratingBarTextView)

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            ("Aha, du wählst $rating").also { ratingBarTextView.text = it }
        }
    }


    private fun setupSpinner(view: View?) {

            val spinner = view?.findViewById<View>(R.id.spinner) as Spinner?

            spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?, // important: this has to be Nullable
                    position: Int,
                    id: Long
                ) {
                    if (!firstTimeSpinnerSelected) {

                        val selectedItem = parent?.getItemAtPosition(position) as String?
                        Log.d(TAG, String.format("Selected %s", selectedItem))
                        val toastMessage = "Du hast $selectedItem ausgewählt"
                        Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
                    }
                    firstTimeSpinnerSelected = false
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

    }

    private fun setupAlertdialog(view: View, savedInstanceState: Bundle?) {
        val btnStartDialog = view.findViewById<Button>(R.id.btnStartDialog)
        val dialog = onCreateDialog(savedInstanceState)
        btnStartDialog.setOnClickListener { dialog.show()  }
    }




    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.i(TAG, "onCreateDialog")
        val items = arrayOf("Alles", "Ein bisschen", "Nichts")

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Was willst du?")
                .setItems(items
                ) { dialog, which ->
                    // The 'which' argument contains the index position
                    // of the selected item
                    val item = items[which]
                    val message = "Soso, du hast $item gewählt"
                    Toast.makeText(context , message, Toast.LENGTH_SHORT).show()

                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
    companion object {
        const val MY_ACTION_SHOW_TEXT = "ch.hslu.mobpro.intentfilterandwidget.SHOW_TEXT"
        const val MY_EXTRA_KEY = "MY_EXTRA_KEY"
    }
}