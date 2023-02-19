package ch.hslu.mobpro.firstappfinal.lifecyclelog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ch.hslu.mobpro.firstappfinal.R

class LifecycleLogFragment : Fragment(R.layout.fragment_lifecycle_logger) {

	companion object {
		fun newInstance(): LifecycleLogFragment {
			return LifecycleLogFragment()
		}
	}


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		Log.d(javaClass.simpleName, "Fragment onCreate() aufgerufen")
	}


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		Log.d(javaClass.simpleName, "Fragment onViewCreated(View view, Bundle savedInstanceState) aufgerufen")
	}


	override fun onPause() {
		super.onPause()
		Log.d(javaClass.simpleName, "Fragment onPause() aufgerufen")
	}

	override fun onResume() {
		super.onResume()
		Log.d(javaClass.simpleName, "Fragment onResume() aufgerufen")
	}


	override fun onStart() {
		super.onStart()
		Log.d(javaClass.simpleName, "Fragment onStart() aufgerufen")
	}

	override fun onStop() {
		super.onStop()
		Log.d(javaClass.simpleName, "Fragment onStop() aufgerufen")
	}


	override fun onDestroy() {
		super.onDestroy()
		Log.d(javaClass.simpleName, "Fragment onDestroy() aufgerufen")
	}

	override fun onDestroyView() {
		super.onDestroyView()
		Log.d(javaClass.simpleName, "Fragment onDestroyView() aufgerufen")
	}

}
