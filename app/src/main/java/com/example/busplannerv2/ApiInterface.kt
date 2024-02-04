package com.example.busplannerv2


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val appID = "688e7d4f"
const val apiKey = "2a9d138c8568d2766e52d94622747a03"

interface ApiInterface {
    @GET("GetNextTripsForStop?appID=$appID&apiKey=$apiKey")
    fun getData(
        @Query("stopNo") stopNo: String,
        @Query("routeNo") routeNo: String,
        @Query("format") format: String = "JSON"
    ): Call<BusAPI>
}