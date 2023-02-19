package com.example.kommunikation_nebenlufigkeit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface BandsApiService {

    @GET("all.json")
    fun getBandNames(): Call<List<BandCode>>

    @GET("info/{code}.json")
    fun getBandInfo(@Path("code") code: String): Call<BandInfo>
}

data class BandCode (
    val name: String,
    val code: String
)

data class BandInfo(
    val name: String,
    val foundingYear: Int,
    val homeCountry: String,
    val bestOfCdCoverImageUrl: String?
)