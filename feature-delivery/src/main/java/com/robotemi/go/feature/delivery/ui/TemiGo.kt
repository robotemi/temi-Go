package com.robotemi.go.feature.delivery.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.robotemi.go.feature.delivery.model.Tray
import com.robotemi.go.feature.mymodel.R

@Composable
fun TemiGo(
    onSelect: (tray: Tray) -> Unit,
    onCancel: (tray: Tray) -> Unit,
    map: Map<Tray, String?>,
    currentSelectedTray: Tray?
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
            onTrayClicked = { onSelect(Tray.TOP) },
            onXClicked = { onCancel(Tray.TOP) },
            destination = map[Tray.TOP],
            isSelected = currentSelectedTray == Tray.TOP
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
            onTrayClicked = { onSelect(Tray.MIDDLE) },
            onXClicked = { onCancel(Tray.MIDDLE) },
            destination = map[Tray.MIDDLE],
            isSelected = currentSelectedTray == Tray.MIDDLE
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
            onTrayClicked = { onSelect(Tray.BOTTOM) },
            onXClicked = { onCancel(Tray.BOTTOM) },
            destination = map[Tray.BOTTOM],
            isSelected = currentSelectedTray == Tray.BOTTOM
        )
    }

}

@Composable
private fun TrayLayer(
    modifier: Modifier,
    modifierX: Modifier,
    @DrawableRes highlightResource: Int,
    @DrawableRes dimResource: Int,
    onTrayClicked: () -> Unit,
    onXClicked: () -> Unit,
    destination: String?,
    isSelected: Boolean = false,
) {

    val imageResource = if (destination != null || isSelected) highlightResource else dimResource


    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = destination == null) {
                    onTrayClicked()
                },
            contentScale = ContentScale.FillWidth,
            painter = painterResource(imageResource),
            contentDescription = null
        )
        if (destination != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .widthIn(max = 210.dp)
                    .heightIn(max = 57.dp)
            ) {
                Text(
                    text = destination, color = Color.White,
                    textAlign = TextAlign.Center,
                    fontSize = when (destination.length) {
                        in 1..5 -> 60.sp
                        in 6..10 -> 40.sp
                        else -> 30.sp
                    },
                )
            }
        }
    }

    if (destination != null) {
        ButtonX(modifier = modifierX
            .offset(x = (-10).dp)
            .clickable {
                onXClicked()
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


//@Preview(showBackground = true, widthDp = 1706, heightDp = 904)
//@Composable
//private fun PortraitPreview() {
//    MyApplicationTheme {
//        TemiGoNew(changeTray = {},
//            removeTrayLocation = {},
//            map = mapOf(),
//            currentSelectedTray = Tray.EMPTY,
//            onSelect = {tray -> currentSelectedTray = if(currentSelectedTray != tray) tray else null },
//            onCancel = {tray -> removeTrayLocation(tray)},)
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