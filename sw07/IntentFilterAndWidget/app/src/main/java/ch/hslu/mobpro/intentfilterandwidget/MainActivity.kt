package ch.hslu.mobpro.intentfilterandwidget

import android.app.AlertDialog
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ch.hslu.mobpro.intentfilterandwidget.databinding.ActivityMainBinding
import java.util.*

class MainActivity: AppCompatActivity() {


    private val TAG = "MyBrowsingActivity"

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val api = android.os.Build.VERSION.SDK_INT.toString()
        Log.d(TAG,  "current SDK_INT = $api")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainButtonStartIntent.setOnClickListener { startBrowserIntentOnClick() }
        binding.mainButtonqueryBrowserIntent.setOnClickListener { queryBrowserIntentOnClick() }
        binding.mainButtonIntentWithCustom.setOnClickListener { startCustomIntentOnClick() }

        binding.mainButtonUpdateWidgetText.setOnClickListener { updateWidgetTextOnClick(applicationContext) }

        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setIcon(R.mipmap.ic_launcher)

        displayIntentIfExists()
    }

    private fun updateWidgetTextOnClick(context: Context) {

        val newText = binding.textViewUpdateWidget.text

        val sharedPref = getSharedPreferences(MyAppWidgetProvider.MY_PREFERENCES_NAME, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(MyAppWidgetProvider.WIDGET_TEXT_PREFS_KEY, newText.toString())
            apply()
        }

        requestWidgetUpdate(context)
    }

    private fun startBrowserIntentOnClick() {
        startActivity(createHsluBrowserIntent)
    }

    private val createHsluBrowserIntent: Intent
        get() {
            val intent = Intent(this, MyBrowsingActivity::class.java)
            intent.data = Uri.parse("https://www.hslu.ch/de-ch")
            return intent
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




    private fun displayIntentIfExists() {
        val intent = intent
        if (MY_ACTION_SHOW_TEXT.contentEquals(intent.action!!)) {
            Log.d(TAG, "HERE")
            binding.textViewCustomIntent.text = intent.extras?.getString(MY_EXTRA_KEY)
        }
    }

    private fun queryBrowserIntentOnClick() {
        val resolveList = packageManager
            .queryIntentActivities(createHsluBrowserIntent,
        PackageManager.MATCH_ALL)

        val dialogBulider = getDialogBuilderFromStringList(resolveList.map { resolveInfo ->
            resolveInfo.activityInfo.name })
        dialogBulider.create().show()
    }


    private fun getDialogBuilderFromStringList(stringList: List<String>) : AlertDialog.Builder {
        val dialogBuilder = AlertDialog.Builder(this)
            .setTitle("Alle Browser Activities gemäss Intent Abfrage").setItems(stringList.toTypedArray()
            ) { _, _ ->
                // Intentionally left blank
            }.setNegativeButton("Danke, PackageManager"
            ) { _, _ ->
                // Intentionally left blank
            }
        return dialogBuilder
    }

    companion object {
        const val MY_ACTION_SHOW_TEXT = "ch.hslu.mobpro.intentfilterandwidget.SHOW_TEXT"
        const val MY_EXTRA_KEY = "MY_EXTRA_KEY"

        fun requestWidgetUpdate(context: Context) {
            val widget = ComponentName(context, MyAppWidgetProvider::class.java)
            val appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(widget)
            val updateWidget = Intent(context, MyAppWidgetProvider::class.java)
            updateWidget.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            updateWidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            context.sendBroadcast(updateWidget)
        }
    }
}