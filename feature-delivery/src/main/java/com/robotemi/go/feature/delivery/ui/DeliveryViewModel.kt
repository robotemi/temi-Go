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

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.robotemi.go.core.data.LocationRepository
import com.robotemi.go.feature.delivery.model.Tray
import com.robotemi.go.feature.delivery.ui.DeliveryScreenUiState.Error
import com.robotemi.go.feature.delivery.ui.DeliveryScreenUiState.Loading
import com.robotemi.go.feature.delivery.ui.DeliveryScreenUiState.Success
import com.robotemi.sdk.Robot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MyModelViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val robot by lazy { Robot.getInstance() }

    private val _uiStateInternal: StateFlow<DeliveryScreenUiState> = locationRepository
        .locations.map<List<String>, DeliveryScreenUiState> { Success(locations = it) }
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    private val _uiState: MutableStateFlow<DeliveryScreenUiState> = MutableStateFlow(_uiStateInternal.value)

    val uiState: StateFlow<DeliveryScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiStateInternal.collect {
                _uiState.emit(it)
            }
        }
    }

    fun setTrayLocation(location: String) {
        _uiState.update { currentState ->
            if (currentState is Success && currentState.currentSelectedTray != null) {
                val newTray = currentState.tray.toMutableMap()
                newTray[currentState.currentSelectedTray!!] = location
                currentState.copy(tray = newTray, currentSelectedTray = null)
            } else {
                currentState
            }
        }
        Log.d("MyModelViewModel", "${(uiState.value as Success).tray}")
    }

    fun removeTrayLocation(tray: Tray) {
        _uiState.update { currentState ->
            if (currentState is Success) {
                val newTray = currentState.tray.toMutableMap()
                newTray.remove(tray)
                currentState.copy(tray = newTray)
            } else {
                currentState
            }
        }
        Log.d("MyModelViewModel", "${(uiState.value as Success).tray}")
    }

    fun setCurrentSelectedTray(tray: Tray?){
        _uiState.update { currentState ->
            if (currentState is Success) {
                currentState.copy(currentSelectedTray = if (currentState.currentSelectedTray == tray) null else tray)
            } else {
                currentState
            }
        }
    }

    fun go(){
        val locationTop = (uiState.value as Success).tray[Tray.TOP]
        val locationMiddle = (uiState.value as Success).tray[Tray.MIDDLE]
        val locationBottom = (uiState.value as Success).tray[Tray.BOTTOM]

        val goList = mutableListOf<String>()
        if (locationTop != null) goList.add(locationTop)
        if (locationMiddle != null) goList.add(locationMiddle)
        if (locationBottom != null) goList.add(locationBottom)

        Log.d("MyModelViewModel", "goList: $goList")

        robot.goTo(goList[0])
    }

}

sealed class DeliveryScreenUiState {
    data object Loading : DeliveryScreenUiState()
    data class Error(val throwable: Throwable) : DeliveryScreenUiState()
    data class Success(
        val locations: List<String>,
        var tray: MutableMap<Tray, String?> = mutableMapOf(),
        var currentSelectedTray: Tray? = null
    ) : DeliveryScreenUiState()
}

