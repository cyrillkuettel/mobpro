package ch.hslu.mobpro.firstappfinal.lifecyclelog

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ch.hslu.mobpro.firstappfinal.R


class LifecycleLogActivity : AppCompatActivity() {

	private val TAG = "LifecycleLogActivity";
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_lifecycle_logger)
		Log.i(TAG, "Activity onCreate() aufgerufen")
		if (savedInstanceState == null) {
			//TODO show LifecycleLogFragment
			supportFragmentManager.beginTransaction()
				.add(R.id.fragment_host, LifecycleLogFragment.newInstance())
				.commit()
		}
	}


	override fun onStart() {
		super.onStart()
		Log.i(TAG, "Activity onStart() aufgerufen")
	}
	override fun onRestart() {
		super.onRestart()
		Log.i(TAG, "Activity onRestart() aufgerufen")
	}
	override fun onResume() {
		super.onResume()
		Log.i(TAG, "Activity onResume() aufgerufen")
	}
	override fun onStop() {
		super.onStop()
		Log.i(TAG, "Activity onStop() aufgerufen")
	}
	override fun onDestroy() {
		super.onDestroy()
		Log.i(TAG, "Activity onDestroy() aufgerufen")
	}
}