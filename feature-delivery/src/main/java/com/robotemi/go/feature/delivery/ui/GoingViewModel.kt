package com.robotemi.go.feature.delivery.ui

import androidx.lifecycle.ViewModel
import com.robotemi.go.core.data.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GoingViewModel @Inject constructor(
    private val locationRepository: LocationRepository
): ViewModel(){
}