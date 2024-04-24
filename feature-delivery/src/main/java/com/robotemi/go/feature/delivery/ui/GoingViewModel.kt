package com.robotemi.go.feature.delivery.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.robotemi.sdk.Robot
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GoingViewModel(savedStateHandle: SavedStateHandle): ViewModel(), OnGoToLocationStatusChangedListener{
    private val robot = Robot.getInstance()

    val location: String = checkNotNull(savedStateHandle["location"])

    private val _back: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val back = _back.asStateFlow()
    init {
        robot.addOnGoToLocationStatusChangedListener(this)
        robot.goTo(location)
    }
    override fun onGoToLocationStatusChanged(
        location: String,
        status: String,
        descriptionId: Int,
        description: String
    ) {
        if (status == OnGoToLocationStatusChangedListener.COMPLETE) {
            _back.value = true
        }

        if (status == OnGoToLocationStatusChangedListener.ABORT) {
            _back.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        robot.removeOnGoToLocationStatusChangedListener(this)
    }
}