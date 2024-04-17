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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import com.robotemi.go.feature.delivery.ui.DeliveryScreenUiState.Success
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.robotemi.go.feature.delivery.model.Tray
import com.robotemi.go.feature.mymodel.R

@Composable
fun DeliveryScreen(modifier: Modifier = Modifier, viewModel: MyModelViewModel = hiltViewModel()) {
    val items by viewModel.uiState.collectAsState()
    if (items is Success) {
        DeliveryScreen(
            locations = (items as Success).locations,
            map = (items as Success).tray,
            onSave = { name -> viewModel.addMyModel(name) },
            modifier = modifier,
            onMakePair = { layer: Tray, location: String -> viewModel.setTrayLocation(layer, location) },
            removePair = { tray -> viewModel.removeTrayLocation(tray) },
            go = { viewModel.go() }
        )
    }
}

@Composable
internal fun DeliveryScreen(
    locations: List<String>,
    onSave: (name: String) -> Unit,
    modifier: Modifier = Modifier,
    onMakePair: (layer: Tray, location: String) -> Unit,
    removePair: (layer: Tray) -> Unit,
    map: Map<Tray, String>,
    go: () -> Unit
) {
    var currentSelectedTray by remember { mutableStateOf(Tray.EMPTY) }

    Row {
        TemiGoNew(
            changeTray = { layer -> currentSelectedTray = layer },
            removePair = { layer -> removePair(layer) },
            map = map,
            currentSelectedTray = currentSelectedTray
        )


        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            LocationGrid(
                locations = locations,
                onMakePair = onMakePair,
                currentSelectedTray = currentSelectedTray,
                map = map,
                changeTray = { layer -> currentSelectedTray = layer })

            GoButton(map, go)
        }
    }
}

@Composable
fun TemiGo(
    changeTray: (layer: Tray) -> Unit,
    removePair: (layer: Tray) -> Unit,
    map: Map<Tray, String>,
    currentSelectedTray: Tray
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(.3f)
    ) {
        TrayLayer(Tray.TOP, map, changeTray, removePair, currentSelectedTray)
        TrayLayer(Tray.MIDDLE, map, changeTray, removePair, currentSelectedTray)
        TrayLayer(Tray.BOTTOM, map, changeTray, removePair, currentSelectedTray)
    }
}

@Composable
fun TrayLayer(
    layer: Tray,
    map: Map<Tray, String>,
    changeTray: (layer: Tray) -> Unit,
    removePair: (layer: Tray) -> Unit,
    currentSelectedTray: Tray
) {
    Button(
        modifier = Modifier
            .padding(8.dp)
            .size(300.dp, 300.dp),
        onClick = {
            if (map.containsKey(layer)) {
                return@Button
            } else if (currentSelectedTray != layer) {
                changeTray(layer)
            } else {
                changeTray(Tray.EMPTY)
            }
        },
        colors = if (currentSelectedTray == layer || map.containsKey(layer)) ButtonDefaults.buttonColors(
            Color(0xFF20D199)
        ) else ButtonDefaults.buttonColors(Color.Black)
    ) {
        Text(
            text = if (map.containsKey(layer)) map.getValue(layer) else "",
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }

    if (map.containsKey(layer)) {
        Button(
            modifier = Modifier
                .size(50.dp, 50.dp)
                .border(1.dp, Color.Black),
            onClick = {
                removePair(layer)
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

@Composable
fun LocationGrid(
    locations: List<String>,
    onMakePair: (layer: Tray, location: String) -> Unit,
    currentSelectedTray: Tray,
    map: Map<Tray, String>,
    changeTray: (layer: Tray) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
            .offset(0.dp, 150.dp)
            .background(Color(0xFFF8F8F8))
            .width(1054.dp)
            .height(632.dp)
    ) {
        items(items = locations, key = { location -> location }) { location ->
            Box(
                modifier = Modifier
                    .width(194.dp)
                    .height(150.dp)
                    .padding(24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (map.containsValue(location)) {
                            Color(
                                0xFF20D199
                            )
                        } else
                            Color(0xFFBABABA)
                    )
                    .clickable {
                        if (currentSelectedTray != Tray.EMPTY) {
                            onMakePair(currentSelectedTray, location)
                            changeTray(Tray.EMPTY)
                        }
                    },
            ) {
                Text(
                    text = location,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = when (location.length) {
                        in 1..5 -> 60.sp
                        in 6..10 -> 40.sp
                        else -> 30.sp
                    },
                    modifier = Modifier.align(Alignment.Center).padding(horizontal = 5.dp)
                )
            }
        }
    }
}

@Composable
fun GoButton(map: Map<Tray, String>, go: () -> Unit){
    val enabledColor = if (map.isNotEmpty()) Color(0xFF20D199) else Color(0x7520D199)
    Row(modifier = Modifier.clickable(onClick = {
        if (map.isNotEmpty()) {
            go()
        }
    })
        .offset(x = 850.dp, y = 150.dp),
        ) {
        Text(
            text = "GO",
            textAlign = TextAlign.Center,
            fontSize = 100.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
            color = enabledColor
        )
        Image(
            painter = painterResource(id = R.drawable.go_icon),
            contentDescription = "GO",
            modifier = Modifier.align(Alignment.CenterVertically)
        )
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
