package com.example.busplannerv2


data class GetNextTripsForStopResult(
    val Error: String,
    val Route: Route,
    val StopLabel: String,
    val StopNo: String
)