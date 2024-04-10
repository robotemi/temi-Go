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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.robotemi.go.core.data.LocationRepository
import com.robotemi.go.feature.delivery.ui.DeliveryScreenUiState.Error
import com.robotemi.go.feature.delivery.ui.DeliveryScreenUiState.Loading
import com.robotemi.go.feature.delivery.ui.DeliveryScreenUiState.Success
import javax.inject.Inject

@HiltViewModel
class MyModelViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    val uiState: StateFlow<DeliveryScreenUiState> = locationRepository
        .locations.map<List<String>, DeliveryScreenUiState> { Success(data = it) }
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addMyModel(name: String) {
        viewModelScope.launch {
            locationRepository.add(name)
        }
    }
}

sealed interface DeliveryScreenUiState {
    data object Loading : DeliveryScreenUiState
    data class Error(val throwable: Throwable) : DeliveryScreenUiState
    data class Success(val data: List<String>) : DeliveryScreenUiState
}
