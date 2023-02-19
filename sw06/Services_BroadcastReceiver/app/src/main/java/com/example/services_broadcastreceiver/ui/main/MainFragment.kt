package com.example.services_broadcastreceiver.ui.main

import android.app.AlertDialog
import android.content.*
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.services_broadcastreceiver.databinding.FragmentMainBinding
import com.example.services_broadcastreceiver.service.MusicPlayerConnection
import com.example.services_broadcastreceiver.service.MusicPlayerService
import com.example.services_broadcastreceiver.worker.LocalizeMissilesWorker


class MainFragment : Fragment() {

    private val TAG = "MainFragment"
    private val ACTION_MY_BROADCAST = "ACTION_MY_BROADCAST"

    private var serviceConnection: MusicPlayerConnection? = null
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private var myBroadcastReceiver: BroadcastReceiver? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainButtonStartMusicPlayer.setOnClickListener { playerStartClicked() }
        binding.mainStopServiceButton.setOnClickListener { playerStopClicked() }
        binding.mainButtonNextSong.setOnClickListener { playerNextClicked() }
        binding.mainButtonHistory.setOnClickListener { playerShowHistory() }
        binding.mainCheckBoxConnectToService.setOnClickListener { bindServiceCheckboxOnClick() }
        binding.mainCheckBoxBroadcastReceiverRegistered.setOnClickListener { toggleRegisterBroadcastOnClick() }
        binding.mainButtonSendGlobalBroadcast.setOnClickListener { sendGlobalBroadcastIfCheckBoxEnabled() }
        binding.mainButonLocalizeMissiles.setOnClickListener { startBackgroundTaskClicked() }
    }


    private fun toggleRegisterBroadcastOnClick() {
        if (binding.mainCheckBoxBroadcastReceiverRegistered.isChecked) {
            registerBroadcastReceiver()
        } else {
            deregisterBroadcastReceiver()
        }
    }


    private fun playerStartClicked() {
        requireActivity().startService(Intent(context, MusicPlayerService::class.java))

    }

    private fun playerStopClicked() {
        requireActivity().stopService(Intent(context, MusicPlayerService::class.java))
    }


    private fun playerNextClicked() {
        if (isServiceBound()) {
            val next: String? = serviceConnection?.getMusicPlayerApi()?.playNext()
            showToast("Nächstes Stücck: $next")
        } else {
            showToast("Service is noch nicht gebunden. \n Keine Kommunikation möglich")
        }
    }

    private fun playerShowHistory() {
        if (isServiceBound()) {
            val history = serviceConnection?.getMusicPlayerApi()?.queryHistory()

            if (history != null) {
                Log.v(TAG, String.format("history size is %d" , history.size))
            } else {
                Log.v(TAG, "history == null")

            }

            AlertDialog.Builder(context)
                .setTitle("History")
                .setIcon(android.R.drawable.ic_media_rew)
                .setItems(history?.toTypedArray<CharSequence>(), null)
                .setNeutralButton("Ok, got it", null)
                .show()

            Log.i(TAG, "Printing history: " + history.toString())
        }

    }


    private fun showToast(toastMessage: String) {

        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    }
    private fun showLongToast(toastMessage: String) {

        Toast.makeText(context, toastMessage, Toast.LENGTH_LONG).show()
    }

    private fun isServiceBound(): Boolean {
        return serviceConnection?.getMusicPlayerApi() != null
    }


    private fun bindServiceCheckboxOnClick() {
        if (binding.mainCheckBoxConnectToService.isEnabled) {
            bindService()
        } else {
            unbindService()
        }
    }


    private fun bindService() {
        if (!isServiceBound()) {
            val demoService = Intent(context, MusicPlayerService::class.java)
            serviceConnection = MusicPlayerConnection()
            serviceConnection?.let { it ->
                // automatically creates a Service as long as the binding
                requireActivity().bindService(demoService, it, Context.BIND_AUTO_CREATE)
            }
            binding.mainStopServiceButton.isEnabled = true
        }
    }

    private fun unbindService() {
        if (isServiceBound()) {
            requireActivity().unbindService(serviceConnection as ServiceConnection)
            serviceConnection = null
            binding.mainStopServiceButton.isEnabled = false
        }

    }

    private fun sendGlobalBroadcastIfCheckBoxEnabled() {
        if (!binding.mainCheckBoxBroadcastReceiverRegistered.isChecked) {
            binding.mainBroadcastMessage.text = "Broadcstempfang deaktiviert!!"
        } else {
            sendGlobalBroadcastClicked()
        }
    }

    private fun sendGlobalBroadcastClicked() {
        val globalBroadcast = Intent(ACTION_MY_BROADCAST)
        globalBroadcast.setPackage(requireActivity().packageName)
        requireActivity().sendBroadcast(globalBroadcast)
    }

    private fun registerBroadcastReceiver() {
        myBroadcastReceiver = object : BroadcastReceiver() {
            private var counter = 0
            override fun onReceive(context: Context, intent: Intent) {
                counter++
                binding.mainBroadcastMessage.text = "Broadcast #$counter erhalten"
            }
        }
        val filter = IntentFilter(ACTION_MY_BROADCAST)
        myBroadcastReceiver?.let { it ->
            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(it, filter)
        }
    }

    private fun deregisterBroadcastReceiver() {
        if (myBroadcastReceiver != null) {
            myBroadcastReceiver?.let { it ->
                LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(it)
            }
            myBroadcastReceiver = null
        }
    }

    private fun startBackgroundTaskClicked() {
        val getLocationTask = OneTimeWorkRequest.Builder(LocalizeMissilesWorker::class.java).build()
        WorkManager.getInstance(requireContext()).enqueue(getLocationTask)

        val workInfo: LiveData<WorkInfo> =
            WorkManager.getInstance(requireContext()).getWorkInfoByIdLiveData(getLocationTask.id)


        workInfo.observe(viewLifecycleOwner, Observer { workInfo ->
            if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                val missilePositions: Array<String>? =
                    workInfo.outputData.getStringArray("missilePositions")
                    missilePositions?.let {missilePositionsList ->
                        if (missilePositionsList.isNotEmpty()) {
                            showLongToast(
                                String.format(
                                    "--- Incoming! ---\nDetected %d missile in-air.\n\nPositions are: \n\n%s",
                                    missilePositionsList.size,
                                    missilePositionsList.joinToString("\n")
                                )
                            )
                        } else {
                            showLongToast("No missiles detected")
                        }
                    }
            }
        })

    }
}

