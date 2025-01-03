package com.example.finalyearproject.utils

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

class LabelEncoder(private val context: Context) {
    private var labels: List<String> = emptyList()
    private var isInitialized = false

    init {
        loadLabels()
    }

    private fun loadLabels() {
        try {
            // Read labels from assets
            context.assets.open("label_classes.txt").use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    labels = reader.readLines()
                    isInitialized = true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            isInitialized = false
        }
    }

    fun encode(label: String): Int {
        if (!isInitialized) return -1
        return labels.indexOf(label)
    }

    fun decode(index: Int): String {
        if (!isInitialized || index < 0 || index >= labels.size) return "unknown"
        return labels[index]
    }

    fun getAllLabels(): List<String> = labels

    fun isReady(): Boolean = isInitialized
}