package com.example.busplannerv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.gson.Gson
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



            val stopTextEdit = findViewById<EditText>(R.id.stopNumber)
            val stopTextContent = stopTextEdit.text
            val routeTextEdit = findViewById<EditText>(R.id.routeNumber)
            val routeTextContent = routeTextEdit.text

            if(stopTextContent.length != 4){
                Log.d("stop != 4", "Stop is not equal to 4")
                myString.append("Please enter a valid stop number (4 numbers)")
                updateText.text = myString

            }else if(routeTextContent.length > 3){
                myString.append("Please enter a valid route number ( 3 or less numbers)")
                updateText.text = myString
            }else {

                Log.d("TextContent", "$stopTextContent")
                val retrofitData =
                    retrofitBuilder.getData(stopTextContent.toString(), routeTextContent.toString())

                retrofitData.enqueue(object : Callback<BusAPI?> {
                    override fun onResponse(call: Call<BusAPI?>, response: Response<BusAPI?>) {
                        val responseBody = response.body()!!
                        val myStringBuilder = StringBuilder()
                        val stopName = responseBody.GetNextTripsForStopResult.StopLabel
                        val stopNo = responseBody.GetNextTripsForStopResult.StopNo
                        Log.d("JSON Object", "${responseBody.GetNextTripsForStopResult.Route}")
                        val Route = responseBody.GetNextTripsForStopResult.Route.toString()
                        val RouteJSON = responseBody.GetNextTripsForStopResult.Route
                        val errorAPI1 = responseBody.GetNextTripsForStopResult.Error
                        when(errorAPI1){
                            "10" ->{
                                myStringBuilder.append("Please enter a valid stop number")
                            }
                            "11" ->{
                                myStringBuilder.append("Please enter a valid route number")
                            }
                            "" ->{
                                if (Route[18].toString() == "[") {
                                    try {


                                        val data = Gson().fromJson(
                                            responseBody.GetNextTripsForStopResult.Route,
                                            RouteList::class.java
                                        )

                                        Log.d("data", "${data.RouteDirection}")

                                        for (routes in data.RouteDirection) {
                                            val route = routes.RouteLabel
                                            val routeNo = routes.RouteNo



                                            Log.d("responseBody", "$responseBody")
                                            myStringBuilder.append("Stop: $stopName ($stopNo) \n")
                                            myStringBuilder.append("Route no.$routeNo, \nDirection: $route\n")

                                            for (trips in routes.Trips.Trip) {
                                                val currentDate = Calendar.getInstance()
                                                val lastUpdated = trips.AdjustmentAge
                                                val ETA = trips.AdjustedScheduleTime
                                                currentDate.add(Calendar.MINUTE, ETA.toInt())
                                                myStringBuilder.append(
                                                    "Next arrival: $ETA minutes (${
                                                        sdf.format(
                                                            currentDate.time
                                                        )
                                                    })\n"
                                                )
                                                if (lastUpdated != "-1") {
                                                    val lastUpdatedDouble = lastUpdated.toDouble()
                                                    val lastUpdatedSecond =
                                                        round(lastUpdatedDouble * 60).toInt()
                                                    myStringBuilder.append("Last updated: $lastUpdatedSecond seconds ago\n")

                                                }
                                            }

                                        }
                                    } catch (ex: Exception) {
                                        myStringBuilder.append("Something went wrong. Please try again later")
                                    }


                                } else if (Route[18].toString() == "{") {
                                    try {

                                        Log.d("Route[18]", "NO")

                                        val data = Gson().fromJson(
                                            responseBody.GetNextTripsForStopResult.Route,
                                            ApiResponse::class.java
                                        )

                                        Log.d("data", "${data.RouteDirection}")
                                        val route = data.RouteDirection.RouteLabel
                                        val routeNo = data.RouteDirection.RouteNo


                                        Log.d("responseBody", "$responseBody")
                                        myStringBuilder.append("Stop: $stopName ($stopNo) \n")
                                        myStringBuilder.append("Route no.$routeNo, \nDirection: $route\n")
                                        for (trips in data.RouteDirection.Trips.Trip) {
                                            val currentDate = Calendar.getInstance()
                                            val lastUpdated = trips.AdjustmentAge
                                            val ETA = trips.AdjustedScheduleTime
                                            currentDate.add(Calendar.MINUTE, ETA.toInt())
                                            myStringBuilder.append(
                                                "Next arrival: $ETA minutes (${
                                                    sdf.format(
                                                        currentDate.time
                                                    )
                                                })\n"
                                            )
                                            if (lastUpdated != "-1") {
                                                val lastUpdatedDouble = lastUpdated.toDouble()
                                                val lastUpdatedSecond =
                                                    round(lastUpdatedDouble * 60).toInt()
                                                myStringBuilder.append("Last updated: $lastUpdatedSecond seconds ago\n")

                                            }
                                        }
                                    } catch (ex: Exception) {
                                        myStringBuilder.append("Something went wrong. Please try again later.")
                                    }
                                }

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
        }

        goButton.setOnClickListener{
            getData1()
        }
    }

}
