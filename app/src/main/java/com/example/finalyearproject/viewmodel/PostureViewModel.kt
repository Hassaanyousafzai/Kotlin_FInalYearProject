package com.example.finalyearproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finalyearproject.data.models.PostureData
import com.example.finalyearproject.utils.PostureClassifier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class PostureViewModel(
    private val postureClassifier: PostureClassifier
) : ViewModel() {
    private val _postureState = MutableStateFlow<PostureData?>(null)
    val postureState: StateFlow<PostureData?> = _postureState

    private val _sittingDuration = MutableStateFlow(0L)
    val sittingDuration: StateFlow<Long> = _sittingDuration

    private val _modelState = MutableStateFlow<ModelState>(
        if (postureClassifier.isReady()) ModelState.Ready else ModelState.NotLoaded
    )
    val modelState: StateFlow<ModelState> = _modelState

    private var sittingStartTime: Long? = null

    sealed class ModelState {
        object Ready : ModelState()
        object NotLoaded : ModelState()
        data class Error(val message: String) : ModelState()
    }

    fun processPostureData(sensorValues: List<Float>) {
        viewModelScope.launch {
            if (!postureClassifier.isReady()) {
                _modelState.value = ModelState.NotLoaded
                return@launch
            }

            val isOccupied = sensorValues.any { it > 30 }

            if (isOccupied) {
                if (sittingStartTime == null) {
                    sittingStartTime = System.currentTimeMillis()
                }

                val predictedPosture = postureClassifier.classifyPosture(sensorValues)
                _postureState.value = PostureData(
                    sensorValues = sensorValues,
                    predictedPosture = predictedPosture,
                    isOccupied = true
                )

                updateSittingDuration()
            } else {
                sittingStartTime = null
                _postureState.value = PostureData(
                    sensorValues = sensorValues,
                    isOccupied = false
                )
            }
        }
    }

    private fun updateSittingDuration() {
        sittingStartTime?.let { startTime ->
            val currentDuration = System.currentTimeMillis() - startTime
            _sittingDuration.value = TimeUnit.MILLISECONDS.toMinutes(currentDuration)
        }
    }

    override fun onCleared() {
        super.onCleared()
        postureClassifier.close()
    }
}