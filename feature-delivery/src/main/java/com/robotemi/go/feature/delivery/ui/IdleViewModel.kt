/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.robotemi.go.feature.delivery.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.robotemi.go.core.data.LocationRepository
import com.robotemi.go.feature.delivery.model.Tray
import com.robotemi.sdk.Robot
import com.robotemi.sdk.serial.Serial
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class IdleViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val robot = Robot.getInstance()

    var goToLocation = ""

    private val _uiState = MutableStateFlow(IdleScreenUiState())

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            locationRepository.locations.collectLatest {
                _uiState.update { currentState ->
                    currentState.copy(locations = it)
                }
            }
        }
    }

    fun addMyModel(name: String) {
        viewModelScope.launch {
            locationRepository.add(name)
        }
    }

    fun setTrayLocation(location: String) {
        _uiState.update { currentState ->
            if (currentState.currentSelectedTray != null) {
                val newTray = currentState.tray.toMutableMap()
                newTray[currentState.currentSelectedTray!!] = location
                toggleLED(currentState.currentSelectedTray!!, true)
                currentState.copy(tray = newTray, currentSelectedTray = null)
            } else {
                currentState
            }
        }
    }

    fun removeTrayLocation(tray: Tray) {
        _uiState.update { currentState ->
            val newTray = currentState.tray.toMutableMap()
            newTray.remove(tray)
            toggleLED(tray, false)
            currentState.copy(tray = newTray)
        }
    }

    fun setCurrentSelectedTray(tray: Tray?) {
        _uiState.update { currentState ->
            currentState.copy(currentSelectedTray = if (currentState.currentSelectedTray == tray) null else tray)
        }
    }

    private fun toggleLED(tray: Tray, isOn: Boolean) {
        if (isOn) {
            robot.sendSerialCommand(
                Serial.CMD_TRAY_LIGHT,
                byteArrayOf(tray.trayNumber.toByte(), 0X20.toByte(), 0XD1.toByte(), 0X99.toByte())
            )
        } else {
            robot.sendSerialCommand(
                Serial.CMD_TRAY_LIGHT,
                byteArrayOf(tray.trayNumber.toByte(), 0X00, 0X00, 0X00)
            )
        }
    }

    fun setGoToLocation() {
        val locationTop = _uiState.value.tray[Tray.TOP]
        val locationMiddle = _uiState.value.tray[Tray.MIDDLE]
        val locationBottom = _uiState.value.tray[Tray.BOTTOM]

        if (locationTop != null) {
            goToLocation = locationTop
            return
        }

        if (locationMiddle != null) {
            goToLocation = locationMiddle
            return
        }

        if (locationBottom != null) {
            goToLocation = locationBottom
            return
        }
    }
}

data class IdleScreenUiState(
    var locations: List<String> = emptyList(),
    var tray: MutableMap<Tray, String?> = mutableMapOf(),
    var currentSelectedTray: Tray? = null,
)

