package com.example.kommunikation_nebenlufigkeit

import android.annotation.SuppressLint
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.kommunikation_nebenlufigkeit.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private val bandsViewModel: BandsViewModel by viewModels()
    private var demoThread: Thread = createDemoThread(WAITING_TIME_MILLIS)
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainButtonBlockGUI.setOnClickListener{ freeze7Seconds()}
        binding.mainButtonDemoThread.setOnClickListener{ startDemoThread() }
        binding.mainButtonDemoWorker.setOnClickListener{ startDemoWorker()}

        binding.mainButtonResetViewModel.setOnClickListener{ resetViewModel()}
        binding.mainButtonShowBandSelection.setOnClickListener { showBands()}
        binding.mainButtonStartServerRequest.setOnClickListener { startServerRequest() }

        setupViewModelObservers()

    }

    private fun resetViewModel() {
        bandsViewModel.resetBandsData()
    }

    private fun showBands() {
        // show the Dialog again
        showBandsDialog()
    }

    private fun startServerRequest() {
        bandsViewModel.requestBandCodesFromServer()
    }

    fun setupViewModelObservers() {

        bandsViewModel.getBands().observe(this, Observer { names ->
            binding.mainCurrentBandName.text = ""
            binding.mainCurrentBandInfo.text = ""
            binding.mainCurrentBandImage.visibility = View.GONE
            binding.mainNumberOfBands.text = "Bands = " + if(names.isEmpty()) 0 else names.size

            if (names.isNotEmpty()) {
                Log.v(TAG, "Showing Bands Dialog now")
                showBandsDialog()
            } else {
                Log.v(TAG, "names is Empty")
            }
        })


        bandsViewModel.getCurrentBand().observe(this, Observer { bandInfo ->
            binding.mainCurrentBandName.text = bandInfo?.name ?: ""
            if (bandInfo != null) {
                binding.mainCurrentBandInfo.text = bandInfo.homeCountry + ", Gründung: " + bandInfo.foundingYear
            } else {
                binding.mainCurrentBandInfo.text = ""
            }
            if (bandInfo?.bestOfCdCoverImageUrl != null) {
                Picasso.get()
                    .load(bandInfo.bestOfCdCoverImageUrl)
                    .into(binding.mainCurrentBandImage)
                binding.mainCurrentBandImage.visibility = View.VISIBLE
            } else {
                binding.mainCurrentBandInfo.visibility = View.GONE
            }
        })

    }



    private fun showBandsDialog() {
        val bands = bandsViewModel.getBands().value!!
        AlertDialog.Builder(this)
            .setTitle("Welche Band?")
            .setItems(bands.map { bandCode -> bandCode.name }.toTypedArray())
            { _: DialogInterface, itemPos: Int ->
                bandsViewModel.requestBandInfoFromServer(bands[itemPos].code)
            }
            .setNegativeButton(
                "Abbrechen"
            )
            { _, _ -> /* ignore*/ }
            .create()
            .show()
    }

    private fun freeze7Seconds() {
        try {
            Thread.sleep(WAITING_TIME_MILLIS)
        } catch (e: InterruptedException) {
        }
    }

    private fun startDemoThread() {
        if (!demoThread.isAlive) {
            demoThread = createDemoThread(WAITING_TIME_MILLIS)
            demoThread.start()
            binding.mainButtonDemoThread.text = "[Demo Thread läuft....]"
        } else {
           Toast.makeText(this, "DemoThread läuft schon!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createDemoThread(waitingTime: Long) : Thread {
        Log.i(TAG, "createDemoThread")
        return object : Thread("hsluDemoThread") {
            val waitingTime = waitingTime
            override fun run() {
                try {
                    sleep(this.waitingTime)
                    runOnUiThread {
                        binding.mainButtonDemoThread.text = resources.getString(R.string.demo_thread_starten)
                        Toast.makeText(this@MainActivity, "Demo Thread beendet!", Toast.LENGTH_SHORT)
                            .show()
                    }
                } catch (e: InterruptedException) {
                    Log.d(TAG, "caught Interrupted exception!")
                }
            }
        }
    }

    private fun startDemoWorker(){
        Log.i(TAG, "startDemoWorker")
        val workManager = WorkManager.getInstance(application)
        val demoWorkerRequest = OneTimeWorkRequestBuilder<DemoWorker>()
            .setInputData(Data.Builder()
                .putLong(DemoWorker.WAITING_TIME_KEY, WAITING_TIME_MILLIS)
                .build())
            .build()
        workManager.enqueue(demoWorkerRequest)
    }

    companion object {
         const val WAITING_TIME_MILLIS: Long = 7000L
    }


}