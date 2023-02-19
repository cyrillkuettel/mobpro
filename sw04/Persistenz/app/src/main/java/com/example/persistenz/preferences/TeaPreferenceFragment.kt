package com.example.persistenz.preferences

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.persistenz.R


class TeaPreferenceFragment : PreferenceFragmentCompat() {


    companion object {
        fun newInstance(): TeaPreferenceFragment {
            return TeaPreferenceFragment()
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }




}

