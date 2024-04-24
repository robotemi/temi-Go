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
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import com.robotemi.go.feature.delivery.ui.DeliveryScreenUiState.Success
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.robotemi.go.feature.delivery.model.Tray
import com.robotemi.go.feature.mymodel.R


@Composable
fun IdleScreen(modifier: Modifier = Modifier, viewModel: IdleViewModel = hiltViewModel(), navController: NavController) {
    val items by viewModel.uiState.collectAsState()

    if (items is Success) {
        IdleScreen(
            modifier = modifier,
            locations = (items as Success).locations,
            map = (items as Success).tray,
            currentSelectedTray = (items as Success).currentSelectedTray,
            setTrayLocation = { location: String ->
                viewModel.setTrayLocation(
                    location
                )
            },
            removeTrayLocation = { tray -> viewModel.removeTrayLocation(tray) },
            setCurrentSelectedTray = { tray -> viewModel.setCurrentSelectedTray(tray) },
            go = {
                viewModel.setGoToLocation()
                Log.d("DeliveryScreen", "Going to location: ${viewModel.goToLocation}")
                navController.navigate("going/${viewModel.goToLocation}")
                 },
        )
    }
}

@Composable
internal fun IdleScreen(
    modifier: Modifier,
    locations: List<String>,
    setTrayLocation: (location: String) -> Unit,
    setCurrentSelectedTray: (tray: Tray?) -> Unit,
    removeTrayLocation: (tray: Tray) -> Unit,
    map: Map<Tray, String?>,
    currentSelectedTray: Tray?,
    go: () -> Unit
) {

    Row {
        TemiGo(
            onSelect = setCurrentSelectedTray,
            onCancel = removeTrayLocation,
            map = map,
            currentSelectedTray = currentSelectedTray,
        )


        Column(modifier = Modifier.fillMaxSize()) {
            LocationGrid(
                locations = locations,
                onClick = setTrayLocation,
                map = map
            )

            GoButton(modifier = modifier.align(Alignment.End), enable = map.isNotEmpty(), go = go)
        }
    }
}

@Composable
fun LocationGrid(
    locations: List<String>,
    onClick: (location: String) -> Unit,
    map: Map<Tray, String?>,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
            .padding(top = 150.dp)
            .background(Color(0xFFF8F8F8))
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.75f),
    ) {
        items(items = locations, key = { location -> location }) { location ->
            val isSelected = map.containsValue(location)
            Box(
                modifier = Modifier
                    .width(194.dp)
                    .height(150.dp)
                    .padding(24.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (isSelected) {
                            Color(
                                0xFF20D199
                            )
                        } else
                            Color(0xFFBABABA)
                    )
                    .clickable {
                        if (!isSelected) {
                            onClick(location)
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
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 5.dp)
                )
            }
        }
    }
}

@Composable
fun GoButton(modifier: Modifier, enable: Boolean, go: () -> Unit) {
    val enabledColor = if (enable) Color(0xFF20D199) else Color(0x7520D199)
    val infiniteTransition = rememberInfiniteTransition(label = "Go Button")
    val animationValue by infiniteTransition.animateValue(
        initialValue = -5f,
        targetValue = 5f,
        typeConverter = Float.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "Go Button"
    )

    val offSet = if (enable) animationValue else 0f

    Row(
        modifier = modifier
            .clickable(onClick = {
                if (enable) {
                    go()
                }
            }
            )
            .background(Color(0xFFEDEDED))
            .padding(end = 50.dp)
    ) {
        Text(
            text = stringResource(R.string.go),
            textAlign = TextAlign.Center,
            fontSize = 100.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp),
            color = enabledColor
        )
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .align(Alignment.CenterVertically)
                .clip(CircleShape)
                .background(enabledColor)
        ) {
            Image(
                painter = painterResource(id = R.drawable.go_button_arrow),
                contentDescription = null,
                modifier = Modifier
                    .offset(x = offSet.dp)
                    .size(50.dp)
                    .align(Alignment.Center)
            )
        }
    }
}


// Preview
//@Preview(showBackground = true, widthDp = 1706, heightDp = 904)
//@Composable
//private fun PortraitPreview() {
//    MyApplicationTheme {
//        DeliveryScreen(
//            listOf("1", "Bar", "3", "4", "5", "6", "7", "8", "9", "Dining"),
//            onSave = {})
//    }
//}
