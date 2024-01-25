package com.example.busplannerv2

data class RouteDirection(
    val Direction: String,
    val Error: String,
    val RequestProcessingTime: String,
    val RouteLabel: String,
    val RouteNo: String,
    val Trips: Trips
)