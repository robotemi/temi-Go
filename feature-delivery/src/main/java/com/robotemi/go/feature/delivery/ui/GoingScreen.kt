package com.robotemi.go.feature.delivery.ui


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.robotemi.go.feature.mymodel.R

private val gradientColors = listOf(Color(0xFF20D199), Color.White, Color(0xFF20D199))

@Composable
fun GoingScreen(
    modifier: Modifier = Modifier,
    viewModel: GoingViewModel = hiltViewModel(),
    navController: NavController
) {
    val back = viewModel.back.collectAsState()
    if (back.value) {
        navController.popBackStack("idle", false)
    }
    GoingScreen(modifier = modifier, location = viewModel.location,stop = {viewModel.stop()})
}

@Composable
internal fun GoingScreen(
    modifier: Modifier = Modifier,
    location: String,
    stop: () -> Unit
) {
    Column(modifier.fillMaxSize()) {
        Destination(
            modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 150.dp),
            location
        )
        PauseButton(
            modifier
                .align(Alignment.CenterHorizontally)
                .padding(20.dp),
            stop
        )
    }
}

@Composable
private fun Destination(
    modifier: Modifier = Modifier,
    location: String
) {
    Box(
        modifier
            .fillMaxWidth(0.6f)
            .fillMaxHeight(0.6f)
            .drawAnimatedBorder(
                strokeWidth = 8.dp,
                shape = RoundedCornerShape(18.dp),
                durationMillis = 1500
            )
            .background(Color.White)
            .padding(12.dp)
    ) {
        Text(
            text = location,
            modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .align(Alignment.Center),
            color = Color(0xFF20D199),
            fontSize = when (location.length) {
                in 1..5 -> 300.sp
                in 6..10 -> 150.sp
                else -> 100.sp
            },
            fontWeight = FontWeight.Bold,
            lineHeight = 200.sp,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
private fun PauseButton(modifier: Modifier = Modifier, stop: () -> Unit) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .clickable { stop() }) {
        Text(
            text = stringResource(R.string.pause),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4A4A4A),
            fontSize = 70.sp,
            modifier = modifier.padding(10.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.pause),
            contentDescription = "pause",
            modifier
                .width(70.dp)
                .height(70.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

private fun Modifier.drawAnimatedBorder(
    strokeWidth: Dp,
    shape: Shape,
    brush: (Size) -> Brush = {
        Brush.sweepGradient(gradientColors)
    },
    durationMillis: Int
) = composed {

    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotation"
    )

    Modifier
        .clip(shape)
        .drawWithCache {
            val strokeWidthPx = strokeWidth.toPx()

            val outline: Outline = shape.createOutline(size, layoutDirection, this)

            onDrawWithContent {
                drawContent()

                with(drawContext.canvas.nativeCanvas) {
                    val checkPoint = saveLayer(null, null)

                    drawOutline(
                        outline = outline,
                        color = Color.Gray,
                        style = Stroke(strokeWidthPx * 2)
                    )

                    rotate(angle) {

                        drawCircle(
                            brush = brush(size),
                            radius = size.width,
                            blendMode = BlendMode.SrcIn,
                        )
                    }
                    restoreToCount(checkPoint)
                }
            }
        }
}

//@Preview
//@Composable
//fun DestinationPreview() {
//    Destination()
//}
//
//@Preview
//@Composable
//fun PauseButtonPreview() {
//    PauseButton()
//}