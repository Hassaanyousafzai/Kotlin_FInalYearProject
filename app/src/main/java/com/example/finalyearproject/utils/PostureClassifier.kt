package com.example.finalyearproject.utils

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.channels.FileChannel

class PostureClassifier(
    private val context: Context,
    private val labelEncoder: LabelEncoder
) {
    private var interpreter: Interpreter? = null
    private val modelFile = "posture_detection_model.tflite"
    private var isModelLoaded = false

    init {
        try {
            loadModel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun loadModel() {
        try {
            context.assets.openFd(modelFile).use { fileDescriptor ->
                FileInputStream(fileDescriptor.fileDescriptor).use { inputStream ->
                    val startOffset = fileDescriptor.startOffset
                    val declaredLength = fileDescriptor.declaredLength
                    inputStream.channel.map(
                        FileChannel.MapMode.READ_ONLY,
                        startOffset,
                        declaredLength
                    ).also { mappedByteBuffer ->
                        interpreter = Interpreter(mappedByteBuffer)
                        isModelLoaded = true
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            isModelLoaded = false
        }
    }

    fun classifyPosture(sensorValues: List<Float>): String {
        if (!isModelLoaded || !labelEncoder.isReady()) {
            return "model_not_loaded"
        }

        return try {
            val inputArray = sensorValues.toFloatArray()
            val outputArray = Array(1) { FloatArray(labelEncoder.getAllLabels().size) }

            interpreter?.run(inputArray, outputArray)
            val maxIndex = outputArray[0].indices.maxByOrNull { outputArray[0][it] } ?: 0
            labelEncoder.decode(maxIndex)
        } catch (e: Exception) {
            e.printStackTrace()
            "error"
        }
    }

    fun isReady(): Boolean = isModelLoaded && labelEncoder.isReady()

    fun close() {
        try {
            interpreter?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}