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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.key.Key.Companion.I
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.robotemi.go.feature.mymodel.R

@Composable
fun GoingScreen(modifier: Modifier = Modifier, viewModel: GoingViewModel = hiltViewModel()) {
    GoingScreen(modifier)
}

@Composable
internal fun GoingScreen(
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize()){
        Destination(
            modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 100.dp))
        PauseButton(
            modifier
                .align(Alignment.CenterHorizontally)
                .padding(20.dp))
    }
}

@Composable
fun Destination(
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .fillMaxWidth(0.6f)
            .fillMaxHeight(0.6f)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .drawRainbowBorder(16.dp, 1500)){
        Text(text = "1", modifier = Modifier.align(Alignment.Center), color = Color(0xFF20D199), fontSize = 400.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PauseButton(modifier: Modifier = Modifier) {
    Row(modifier = modifier.clickable { /*TODO*/ }) {
        Text(text = "PAUSE", fontWeight = FontWeight.Bold, color = Color(0xFF4A4A4A), fontSize = 70.sp, modifier = modifier.padding(10.dp))
        Image(painter = painterResource(id = R.drawable.pause), contentDescription = "pause", modifier.width(70.dp).height(70.dp).align(Alignment.CenterVertically))
    }
}

fun Modifier.drawRainbowBorder(
    strokeWidth: Dp,
    durationMillis: Int
) = this.composed {

    val infiniteTransition = rememberInfiniteTransition(label = "border")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "border"
    )

    val brush = Brush.sweepGradient(listOf(Color.White, Color(0xFF20D199)))

    this.drawWithContent {

        val strokeWidthPx = strokeWidth.toPx()
        val width = size.width
        val height = size.height

        drawContent()

        with(drawContext.canvas.nativeCanvas) {
            val checkPoint = saveLayer(null, null)

            // Destination
            drawRect(
                color = Color.Gray,
                topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
                size = Size(width - strokeWidthPx, height - strokeWidthPx),
                style = Stroke(strokeWidthPx)
            )

            // Source
            rotate(angle) {

                drawCircle(
                    brush = brush,
                    radius = size.width,
                    blendMode = BlendMode.SrcIn,
                )
            }

            restoreToCount(checkPoint)
        }
    }
}

@Preview
@Composable
fun DestinationPreview() {
    Destination()
}

@Preview
@Composable
fun PauseButtonPreview() {
    PauseButton()
}