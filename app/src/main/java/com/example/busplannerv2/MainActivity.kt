package com.example.busplannerv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.math.round

var myString = StringBuilder()

const val BASE_URL = "https://api.octranspo1.com/v2.0/"


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val goButton = findViewById<Button>(R.id.goButton)
        val updateText = findViewById<TextView>(R.id.busAPI)
        val sdf = SimpleDateFormat("hh:mm")

        fun getData1() {
            val retrofitBuilder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(ApiInterface::class.java)

            val retrofitData = retrofitBuilder.getData()

            retrofitData.enqueue(object : Callback<BusAPI?> {
                override fun onResponse(call: Call<BusAPI?>, response: Response<BusAPI?>) {
                    Log.d("onResponse","Inside onResponse")
                    val responseBody = response.body()!!
                    Log.d("response.body", "$responseBody")
                    val myStringBuilder = StringBuilder()
                    val stopName = responseBody.GetNextTripsForStopResult.StopLabel
                    val stopNo = responseBody.GetNextTripsForStopResult.StopNo
                    val route = responseBody.GetNextTripsForStopResult.Route.RouteDirection.RouteLabel
                    val routeNo = responseBody.GetNextTripsForStopResult.Route.RouteDirection.RouteNo
                    myStringBuilder.append("Stop: $stopName ($stopNo) \n" )
                    myStringBuilder.append("Route no.$routeNo, \nDirection: $route\n")
                    for(trips in responseBody.GetNextTripsForStopResult.Route.RouteDirection.Trips.Trip){
                        val currentDate = Calendar.getInstance()
                        val lastUpdated = trips.AdjustmentAge
                        val ETA = trips.AdjustedScheduleTime
                        currentDate.add(Calendar.MINUTE, ETA.toInt())
                        myStringBuilder.append("Next arrival: $ETA minutes (${sdf.format(currentDate.time)})\n")
                        if(lastUpdated != "-1") {
                            val lastUpdatedDouble = lastUpdated.toDouble()
                            val lastUpdatedSecond = round(lastUpdatedDouble*60).toInt()
                            myStringBuilder.append("Last updated: $lastUpdatedSecond seconds ago\n")

                        }
                    }



                    myString = myStringBuilder
                    updateText.text = myString
                    Log.d("myStringBuilder", "$myStringBuilder")
                }

                override fun onFailure(call: Call<BusAPI?>, t: Throwable) {
                    TODO("TESTTTTTTTTT")
                }
            })
        }

        goButton.setOnClickListener{
            getData1()
        }
    }

}