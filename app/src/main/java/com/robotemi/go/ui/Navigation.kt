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

package com.robotemi.go.ui


import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.robotemi.go.feature.delivery.ui.IdleScreen
import com.robotemi.go.feature.delivery.ui.GoingScreen

@Composable
fun MainNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "idle") {
        composable("idle") {
            BackHandler(true) {
            }
            IdleScreen(navController = navController)
        }
        // TODO: Add more destinations
        composable(
            "going/{location}",
            arguments = listOf(navArgument("location") { type = NavType.StringType })
        ) { GoingScreen(navController = navController) }


    }
}
