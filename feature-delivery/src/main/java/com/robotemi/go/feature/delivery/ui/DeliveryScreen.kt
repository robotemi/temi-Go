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

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import com.robotemi.go.feature.delivery.ui.DeliveryScreenUiState.Success
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DeliveryScreen(modifier: Modifier = Modifier, viewModel: MyModelViewModel = hiltViewModel()) {
    val items by viewModel.uiState.collectAsState()
    if (items is Success) {
        DeliveryScreen(
            locations = (items as Success).locations,
            locationSelectable = (items as Success).locationClickable,
            map = (items as Success).tray,
            onSave = { name -> viewModel.addMyModel(name) },
            modifier = modifier,
            onSelectTray = { isOn -> viewModel.setSelectable(isOn) },
            onMakePair = { tray: String, location: String -> viewModel.makePair(tray, location) },
            removePair = {tray -> viewModel.removePair(tray) },
            go = { viewModel.go() }
        )
    }
}

@Composable
internal fun DeliveryScreen(
    locations: List<String>,
    locationSelectable: Boolean,
    onSave: (name: String) -> Unit,
    modifier: Modifier = Modifier,
    onSelectTray: (isOn: Boolean) -> Unit,
    onMakePair: (tray: String, location: String) -> Unit,
    removePair: (tray: String) -> Unit,
    map: Map<String, String>,
    go: () -> Unit
) {
    var currentSelectedTry by remember { mutableStateOf("") }
    var isSelectedTray by remember { mutableStateOf(false) }

    Row {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(.3f)
        ) {
            Row {
                Button(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(300.dp, 300.dp),
                    onClick = {
                        if (map.containsKey("top")){
                            return@Button
                        } else if (!isSelectedTray) {
                            currentSelectedTry = "top"
                            isSelectedTray = true
                            onSelectTray(true)
                        } else if (currentSelectedTry == "top") {
                            currentSelectedTry = ""
                            isSelectedTray = false
                            onSelectTray(false)
                        }
                    },
                    colors = if (currentSelectedTry == "top" || map.containsKey("top")) ButtonDefaults.buttonColors(
                        Color.Green
                    ) else ButtonDefaults.buttonColors(Color.Black)
                ) {
                    Text(
                        text = if (map.containsKey("top")) map.getValue("top") else "",
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
                if(map.containsKey("top")) {
                    Button(
                        modifier = Modifier
                            .size(50.dp, 50.dp)
                            .border(1.dp, Color.Black),
                        onClick = {
                            removePair("top")
                        },
                        colors = ButtonDefaults.buttonColors(Color.White)
                    ) {
                        Text(
                            text = "X",
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Row {
                Button(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(300.dp, 300.dp),
                    onClick = {
                        if (map.containsKey("middle")){
                            return@Button
                        } else if (!isSelectedTray) {
                            currentSelectedTry = "middle"
                            isSelectedTray = true
                            onSelectTray(true)
                        } else if (currentSelectedTry == "middle") {
                            currentSelectedTry = ""
                            isSelectedTray = false
                            onSelectTray(false)
                        }
                    },
                    colors = if (currentSelectedTry == "middle" || map.containsKey("middle")) ButtonDefaults.buttonColors(
                        Color.Green
                    ) else ButtonDefaults.buttonColors(Color.Black)
                ) {
                    Text(
                        text = if (map.containsKey("middle")) map.getValue("middle") else "",
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
                if(map.containsKey("middle")) {
                    Button(
                        modifier = Modifier
                            .size(50.dp, 50.dp)
                            .border(1.dp, Color.Black),
                        onClick = {
                            removePair("middle")
                        },
                        colors = ButtonDefaults.buttonColors(Color.White)
                    ) {
                        Text(
                            text = "X",
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Row {
                Button(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(300.dp, 300.dp),
                    onClick = {
                        if (map.containsKey("bottom")){
                            return@Button
                        } else if (!isSelectedTray) {
                            currentSelectedTry = "bottom"
                            isSelectedTray = true
                            onSelectTray(true)
                        } else if (currentSelectedTry == "bottom") {
                            currentSelectedTry = ""
                            isSelectedTray = false
                            onSelectTray(false)
                        }
                    },
                    colors = if (currentSelectedTry == "bottom" || map.containsKey("bottom")) ButtonDefaults.buttonColors(
                        Color.Green
                    ) else ButtonDefaults.buttonColors(Color.Black)
                ) {
                    Text(
                        text = if (map.containsKey("bottom")) map.getValue("bottom") else "",
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
            if(map.containsKey("bottom")) {
                Button(
                    modifier = Modifier
                        .size(50.dp, 50.dp)
                        .border(1.dp, Color.Black),
                    onClick = {
                        removePair("bottom")
                    },
                    colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(
                        text = "X",
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }


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
                        modifier = Modifier.padding(8.dp),
                        colors = if (map.containsValue(location)) ButtonDefaults.buttonColors(Color.Green) else ButtonDefaults.buttonColors(
                            Color.Black
                        ),
                        onClick = {
                            if (isSelectedTray) {
                                onMakePair(currentSelectedTry, location)
                                currentSelectedTry = ""
                                isSelectedTray = false
                            }
                        }) {
                        Text(text = location)
                    }
                }
            }

            val enabledColor = if (map.isNotEmpty()) Color.Green else Color.Gray
            Button(
                modifier = Modifier.align(Alignment.End).width(100.dp).height(100.dp).padding(16.dp),
                onClick = {
                    go()
                },
                enabled = map.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(enabledColor)) {
                Text(text = "GO")
            }
        }
    }
}


// Preview
//    @Preview(showBackground = true, widthDp = 1706, heightDp = 904)
//    @Composable
//    private fun PortraitPreview() {
//        MyApplicationTheme {
//            DeliveryScreen(
//                listOf("1", "Bar", "3", "4", "5", "6", "7", "8", "9", "Dining"),
//                onSave = {})
//        }
//    }
