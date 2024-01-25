package com.example.busplannerv2

data class Trip(
    val AdjustedScheduleTime: String,
    val AdjustmentAge: String,
    val BusType: String,
    val GPSSpeed: String,
    val LastTripOfSchedule: Boolean,
    val Latitude: String,
    val Longitude: String,
    val TripDestination: String,
    val TripStartTime: String
)