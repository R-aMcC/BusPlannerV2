package com.example.busplannerv2

import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Path
const val appID = "688e7d4f"
const val apiKey = "2a9d138c8568d2766e52d94622747a03"
var stopNo = 4579
var routNo = 82
interface ApiInterface {
    @GET("GetNextTripsForStop?appID=$appID&apiKey=$apiKey&stopNo=4579&routeNo=82&format=JSON")
    fun getData(): Call<BusAPI>
}