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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import com.robotemi.go.core.ui.MyApplicationTheme
import com.robotemi.go.feature.delivery.ui.DeliveryScreenUiState.Success
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DeliveryScreen(modifier: Modifier = Modifier, viewModel: MyModelViewModel = hiltViewModel()) {
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    if (items is Success) {
        DeliveryScreen(
            locations = (items as Success).data,
            onSave = { name -> viewModel.addMyModel(name) },
            modifier = modifier
        )
    }
}

@Composable
internal fun DeliveryScreen(
    locations: List<String>,
    onSave: (name: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row {
        Box(
            modifier = Modifier
                .background(color = Color.Red)
                .align(Alignment.CenterVertically)
                .size(328.dp, 725.dp)
        )
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .background(Color.Gray)
                    .weight(1f)
            ) {
                items(items = locations, key = { location -> location }) { location ->
                    Button(
                        onClick = { },
                    ) {
                        Text(text = location)
                    }
                }
            }

            Button(
                modifier = Modifier.align(Alignment.End),
                onClick = {}) {
                Text(text = "GO")
            }
        }

    }
}

// Preview
@Preview(showBackground = true, widthDp = 1706, heightDp = 904)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        DeliveryScreen(listOf("1", "Bar", "3", "4", "5", "6", "7", "8", "9", "Dining"), onSave = {})
    }
}
