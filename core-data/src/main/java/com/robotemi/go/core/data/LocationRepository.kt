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

package com.robotemi.go.core.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.robotemi.go.core.database.MyModel
import com.robotemi.go.core.database.MyModelDao
import com.robotemi.sdk.Robot
import com.robotemi.sdk.listeners.OnLocationsUpdatedListener
import com.robotemi.sdk.listeners.OnRobotReadyListener
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

interface LocationRepository {
    val locations: Flow<List<String>>

    suspend fun add(name: String)
}

class RealLocationRepository @Inject constructor(
    private val myModelDao: MyModelDao
) : LocationRepository, OnLocationsUpdatedListener, OnRobotReadyListener {

    init {
        Robot.getInstance().addOnRobotReadyListener(this)
        Robot.getInstance().addOnLocationsUpdatedListener(this)
    }

    private val flow = MutableStateFlow<List<String>>(listOf())

    override val locations: Flow<List<String>> = flow

    override suspend fun add(name: String) {
        myModelDao.insertMyModel(MyModel(name = name))
    }

    override fun onLocationsUpdated(locations: List<String>) {
        Log.d("LOCATIONS", "locations $locations")
        flow.value = locations
    }

    override fun onRobotReady(isReady: Boolean) {
        if (isReady) {
            val locations = Robot.getInstance().locations
            Log.d("LOCATIONS", "initial locations $locations")
            flow.value = locations
        }
    }
}
