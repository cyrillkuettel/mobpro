package ch.hslu.mobpro.intentfilterandwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews

class MyAppWidgetProvider : AppWidgetProvider() {
    private val TAG = "MyAppWidgetProvider"

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        Log.v(TAG, "onUpdate")

        val text = getTextFromPrefs(context)
        // Perform this loop procedure for each widget that belongs to this
        // provider.
        appWidgetIds.forEach { appWidgetId ->
            // Create an Intent to launch ExampleActivity.
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0,
               Intent(context, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            val views: RemoteViews = RemoteViews(context.packageName, R.layout.my_app_widget_provider).apply {
                Log.v(TAG, "Setting setTextView of widget: $text")
                setTextViewText(R.id.appWidgetText, "$text \n ${updateCount++}. Akt.")

                setOnClickPendingIntent(R.id.appWidgetButton, pendingIntent)
            }
            // Tell the AppWidgetManager to perform an update on the current
            // widget.
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    private fun getTextFromPrefs(context: Context) : String? {
        return context.getSharedPreferences(MY_PREFERENCES_NAME, Context.MODE_PRIVATE)
            .getString(WIDGET_TEXT_PREFS_KEY, "YOUR_WIDGET_TEXT")
    }

// convenient shorthand for accessing “static” properties/functions
    companion object {
        private var updateCount = 0
        const val MY_PREFERENCES_NAME = "myPrefs"
        const val WIDGET_TEXT_PREFS_KEY = "widgetText"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
            Log.v(TAG, "onReceive")
    }



}

