package com.example.finalyearproject.data.models

data class PostureData(
    val sensorValues: List<Float>,
    val timestamp: Long = System.currentTimeMillis(),
    val predictedPosture: String = "",
    val isOccupied: Boolean = false
)
