package com.example.persistenz.filesystem


import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.persistenz.R
import com.example.persistenz.databinding.FragmentOverviewBinding
import com.example.persistenz.preferences.SharedPreferencesViewModel
import com.example.persistenz.preferences.TeaPreferenceFragment


class OverviewFragment : Fragment() {
// R.layout.fragment_overview

    private val TAG = "OverviewFragment"


    private val sharedPreferencesViewModel: SharedPreferencesViewModel by activityViewModels()
    private val fileReaderViewModel: FileReaderViewModel by activityViewModels()

    private var _binding: FragmentOverviewBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            object : ActivityResultCallback<Boolean> {
                override fun onActivityResult(result: Boolean) {
                    if (!result) {
                        Toast.makeText(context, "Permission denied!", Toast.LENGTH_SHORT).show()
                        return
                    } else {
                        readSms()
                    }
                }
            }
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.mainButtonOpenPreferences.setOnClickListener { openPreferencesOnClick() }
        binding.mainButtonSetDefaultPreferences.setOnClickListener { setDefaultSharedPreferencesOnclick() }

        binding.mainButtonLoad.setOnClickListener { onClickLoadFile() }
        binding.mainButtonSave.setOnClickListener { onClickSaveFile() }
        binding.mainCheckboxExternalStorage.setOnClickListener { onClickCheckBox() }
        binding.mainButtonShowSMS.setOnClickListener{
            showSMSList()
        }

        fileReaderViewModel.getStoredText().observe(viewLifecycleOwner, Observer { text ->
            Log.d(TAG, "fileReaderViewModel.getStoredText().observe")
            binding.mainFileText.setText(text)
        })


        // Observer on Settings
        sharedPreferencesViewModel.getPreferencesSummary().observe(viewLifecycleOwner) { text ->
            binding.textViewOnSettingsLoaded.text = text

        }

        fileReaderViewModel.getStorageStateText().observe(viewLifecycleOwner, Observer { text ->
            binding.mainExternalStorageStateText.text = text
        })

        fileReaderViewModel.isExternalStorageEnabled()
            .observe(viewLifecycleOwner, Observer { isEnabled ->
                binding.mainCheckboxExternalStorage.isEnabled = isEnabled
            })

    }

    private fun onClickCheckBox() {
        fileReaderViewModel.updateStorageState()
    }

    private fun onClickSaveFile() {
        Log.v(TAG, "Writing ${binding.mainFileText.text} to Disk.")
        fileReaderViewModel.writeTextToFile(
            binding.mainFileText.text.toString(),
            binding.mainCheckboxExternalStorage.isEnabled
        )
    }

    private fun onClickLoadFile() {
        fileReaderViewModel.readTextFromFile(
            binding.mainCheckboxExternalStorage.isEnabled
        )

    }

    private fun setDefaultSharedPreferencesOnclick() {
        sharedPreferencesViewModel.setDefaultPreferences()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun readSMSWithPermission(permission: String) {
        val result = requireActivity().checkSelfPermission(permission)
        val granted: Boolean = result == PackageManager.PERMISSION_GRANTED
        if (!granted) {
            requestPermissionLauncher.launch(permission)
        } else {
            readSms()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        incrementOnResumeCounter()
        sharedPreferencesViewModel.buildPreferenceSummaryString()
        fileReaderViewModel.updateStorageState()
    }



    private fun openPreferencesOnClick() {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.mainView_replaceFrame, TeaPreferenceFragment.newInstance())
            .addToBackStack("TeaPreferenceFragment")
            .commit()
    }

    private fun incrementOnResumeCounter() {
        val preferences = requireActivity().getSharedPreferences("pref", Context.MODE_PRIVATE)
        // creates if doesn't exist.
        val newCount = preferences.getInt("COUNTER", 0) + 1
        // format the String with the incremented counter
        binding.textViewOnResumeCounter.text = getString(R.string.onresume_counter, newCount)
        val editor = preferences.edit()
        editor.putInt("COUNTER", newCount)
        editor.apply()
    }

    // ----------- Content Provider ------------------------

    @RequiresApi(Build.VERSION_CODES.M)
    private fun showSMSList() {
        readSMSWithPermission(Manifest.permission.READ_SMS)
    }

    private fun readSms() {
        val cursor = requireActivity().contentResolver.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            arrayOf(Telephony.Sms.Inbox._ID, Telephony.Sms.Inbox.BODY),
            null,
            null,
            null
        )
        AlertDialog.Builder(context)
            .setTitle("Sms in Inbox")
            .setCursor(cursor, null, Telephony.TextBasedSmsColumns.BODY)
            .setNeutralButton("Ok", null)
            .create()
            .show()
    }



}