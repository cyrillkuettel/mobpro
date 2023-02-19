package com.example.kommunikation_nebenlufigkeit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.HttpURLConnection

class BandsViewModel : ViewModel() {
    private val TAG = "BandsViewModel"
    private val bands: MutableLiveData<List<BandCode>> = MutableLiveData()
    private val currentBand: MutableLiveData<BandInfo?> = MutableLiveData()

    private val retrofit = Retrofit.Builder()
        .client(
            OkHttpClient()
                .newBuilder()
                .build()
        )
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl("https://wherever.ch/hslu/rock-bands/")
        .build()

    private val bandsService = retrofit.create(BandsApiService::class.java)


    fun getBands(): LiveData<List<BandCode>> {
        return bands
    }

    fun getCurrentBand(): LiveData<BandInfo?> {
        return currentBand
    }


    fun requestBandCodesFromServer() {

        val call = bandsService.getBandNames()
        call.enqueue(object : Callback<List<BandCode>> {
            override fun onResponse(call: Call<List<BandCode>>, response: Response<List<BandCode>>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    bands.value = response.body().orEmpty()
                }
            }

            override fun onFailure(call: Call<List<BandCode>>, t: Throwable) {
                t.printStackTrace()
                Log.e(TAG, "requestBandNamesFromServe onFailure")
            }

        })
    }

    fun requestBandInfoFromServer(code: String) {
        val call = bandsService.getBandInfo(code)
        call.enqueue(object : Callback<BandInfo> {
            override fun onResponse(call: Call<BandInfo>, response: Response<BandInfo>) {
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    currentBand.value = response.body() ?: getDummyBandInfo()
                }
            }

            override fun onFailure(call: Call<BandInfo>, t: Throwable) {
                Log.e(TAG, "requestBandInfoFromServer onFailure")
            }

        })
    }

   private fun getDummyBandInfo(): BandInfo {
        return BandInfo(
            name = "Dummy",
            foundingYear = 666,
            homeCountry = "[home country]",
            bestOfCdCoverImageUrl = null
        )
    }

    fun resetBandsData() {
        currentBand.value = null
        bands.value = emptyList() // don't set to null!
    }


}