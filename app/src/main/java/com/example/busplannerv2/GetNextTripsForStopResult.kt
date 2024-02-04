package com.example.busplannerv2

import com.google.gson.JsonObject


data class GetNextTripsForStopResult(
    val Error: String,
    val Route: JsonObject,
    val StopLabel: String,
    val StopNo: String
)