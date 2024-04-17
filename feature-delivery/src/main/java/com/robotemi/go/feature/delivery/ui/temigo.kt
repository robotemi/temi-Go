package com.robotemi.go.feature.delivery.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.robotemi.go.feature.delivery.model.Tray
import com.robotemi.go.feature.delivery.model.TrayStatus
import com.robotemi.go.feature.mymodel.R

@Composable
fun TemiGoNew(
    onSelect: (tray: Tray) -> Unit,
    onCancel: (tray: Tray) -> Unit,
    map: Map<Tray, String>,
    currentSelectedTray: Tray?,
) {
    ConstraintLayout(
        modifier = Modifier
            .wrapContentSize()
            .aspectRatio(379 / 726f)
    ) {
        val (
            temiGo,
            topTray, middleTray, bottomTray,
            topButtonX, middleButtonX, bottomButtonX,
        ) = createRefs()
        val topTrayGuideLine = createGuidelineFromTop(114 / 726f)
        val middleTrayGuideLine = createGuidelineFromTop(245 / 726f)
        val bottomTrayGuideLine = createGuidelineFromTop(397 / 726f)
        val rightBodyGuidLine = createGuidelineFromStart(329 / 379f)

        Image(
            modifier = Modifier
                .aspectRatio(329 / 726f)
                .constrainAs(temiGo) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(rightBodyGuidLine)
                    width = Dimension.fillToConstraints
                },
            painter = painterResource(id = R.drawable.body),
            contentScale = ContentScale.Fit,
            contentDescription = null
        )

        TrayLayer(
            modifier = Modifier.constrainAs(topTray) {
                top.linkTo(topTrayGuideLine)
                start.linkTo(parent.start)
                end.linkTo(rightBodyGuidLine)
                width = Dimension.fillToConstraints
            },
            modifierX = Modifier.constrainAs(topButtonX) {
                top.linkTo(topTray.top)
                bottom.linkTo(topTray.bottom)
                end.linkTo(parent.end)
            },
            highlightResource = R.drawable.top_highlight,
            dimResource = R.drawable.top_dim,
            tray = Tray.TOP,
            map = map,
            onSelect = onSelect,
            onCancel = onCancel,
            currentSelectedTray = currentSelectedTray,
            destination = map[Tray.TOP] ?: ""
        )

        TrayLayer(
            modifier = Modifier.constrainAs(middleTray) {
                top.linkTo(middleTrayGuideLine)
                start.linkTo(parent.start)
                end.linkTo(rightBodyGuidLine)
                width = Dimension.fillToConstraints
            },
            modifierX = Modifier.constrainAs(middleButtonX) {
                top.linkTo(middleTray.top)
                bottom.linkTo(middleTray.bottom)
                end.linkTo(parent.end)
            },
            highlightResource = R.drawable.middle_highlight,
            dimResource = R.drawable.middle_dim,
            tray = Tray.MIDDLE,
            map = map,
            onSelect = onSelect,
            onCancel = onCancel,
            currentSelectedTray = currentSelectedTray,
            destination = map[Tray.MIDDLE] ?: ""
        )

        TrayLayer(
            modifier = Modifier.constrainAs(bottomTray) {
                top.linkTo(bottomTrayGuideLine)
                start.linkTo(parent.start)
                end.linkTo(rightBodyGuidLine)
                width = Dimension.fillToConstraints
            },
            modifierX = Modifier.constrainAs(bottomButtonX) {
                top.linkTo(bottomTray.top)
                bottom.linkTo(bottomTray.bottom)
                end.linkTo(parent.end)
            },
            highlightResource = R.drawable.bottom_highlight,
            dimResource = R.drawable.bottom_dim,
            tray = Tray.BOTTOM,
            map = map,
            onSelect = onSelect,
            onCancel = onCancel,
            currentSelectedTray = currentSelectedTray,
            destination = map[Tray.BOTTOM] ?: ""
        )
    }

}

@Composable
private fun TrayLayer(
    modifier: Modifier,
    modifierX: Modifier,
    @DrawableRes highlightResource: Int,
    @DrawableRes dimResource: Int,
    tray: Tray,
    map: Map<Tray, String>,
    onSelect: (tray: Tray) -> Unit,
    onCancel: (tray: Tray) -> Unit,
    currentSelectedTray: Tray?,
    destination: String
) {
    var highlightDebug by remember { mutableStateOf(false) } // FIXME, just for debug
    var trayStatus by remember { mutableStateOf(TrayStatus.EMPTY) }

    if(destination != ""){
        trayStatus = TrayStatus.OCCUPIED
    }
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        if (trayStatus == TrayStatus.EMPTY) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if(currentSelectedTray != null){
                            return@clickable
                        }
                        highlightDebug = true
                        trayStatus = TrayStatus.SELECTED
                        onSelect(tray)
                    },
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = dimResource),
                contentDescription = null
            )
        }
    }
    if (trayStatus == TrayStatus.SELECTED) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    highlightDebug = false
                    trayStatus = TrayStatus.EMPTY
                    onSelect(tray)
                },
            contentScale = ContentScale.FillWidth,
            painter = painterResource(id = highlightResource),
            contentDescription = null
        )
    }
    if (trayStatus == TrayStatus.OCCUPIED) {
        Box {
            Image(
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.FillWidth,
                painter = painterResource(id = highlightResource),
                contentDescription = null
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(210.dp)
                    .height(57.dp)
                    .offset(y = 10.dp)
            ) {
                Text(
                    text = destination, color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = when (map.getValue(tray).length) {
                        in 1..5 -> 60.sp
                        in 6..10 -> 40.sp
                        else -> 30.sp
                    },
                )
            }
        }
    }
//        if (highlightDebug) {
//            Image(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable {
//                        highlightDebug = false
//                    },
//                contentScale = ContentScale.FillWidth,
//                painter = painterResource(id = highlightResource),
//                contentDescription = null)
//        } else {
//            Image(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable {
//                        highlightDebug = true
//                        onClick(TrayStatus.SELECTED)
//
//                    },
//                contentScale = ContentScale.FillWidth,
//                painter = painterResource(id = dimResource),
//                contentDescription = null)
//        }

    if (trayStatus == TrayStatus.OCCUPIED) {
        ButtonX(modifier = modifierX.clickable {
            trayStatus = TrayStatus.EMPTY
            onCancel(tray)
        })
    }
}

@Composable
private fun ButtonX(modifier: Modifier) {
    Image(
        modifier = modifier,
        painter = painterResource(id = R.drawable.button_x),
        contentDescription = null
    )
}

//
//@Preview(showBackground = true, widthDp = 1706, heightDp = 904)
//@Composable
//private fun PortraitPreview() {
//    MyApplicationTheme {
//        TemiGoNew(changeTray = {}, removeTrayLocation = {}, map = mapOf(), currentSelectedTray = Tray.EMPTY)
//    }
//}
//
//
//@Preview(showBackground = true, widthDp = 504, heightDp = 1706)
//@Composable
//private fun PortraitPreview2() {
//    MyApplicationTheme {
//        TemiGoNew(changeTray = {}, removeTrayLocation = {}, map = mapOf(), currentSelectedTray = Tray.EMPTY)
//    }
//}