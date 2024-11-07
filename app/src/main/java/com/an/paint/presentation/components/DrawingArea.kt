package com.an.paint.presentation.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.an.paint.domain.model.DrawDetails
import com.an.paint.domain.model.DrawPoint
import com.an.paint.domain.model.toOffset
import com.an.paint.domain.util.Element
import kotlin.math.roundToInt


@Composable
fun DrawingArea(
    modifier: Modifier = Modifier,
    onTap: (Offset) -> Unit,
    elements: List<Element> = emptyList(),
    lastTouchPoint: DrawPoint?,
    selectedElementIndex: Int? = null,
    onTransform: (Float, Float, Offset) -> Unit
) {
    val textMeasurer = rememberTextMeasurer()


    Canvas(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .pointerInput(Unit) {

                detectTapGestures(
                    onTap = { tapOffset ->
                        onTap(tapOffset)
                    },
                )

            }
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, rotation ->
                    onTransform(zoom, rotation, pan)
                    Log.d("TAG", "DrawingArea: zoom: $zoom, rotation: $rotation, centroid: $centroid, pan: $pan")
                }
            }
            .drawBehind {
                drawText(
                    textMeasurer = textMeasurer,
                    text = "${size.width.roundToInt()}x${size.height.roundToInt()}",
                    topLeft = Offset(
                        x = 0f,
                        y = 0f
                    ),
                    style = TextStyle(
                        fontSize = 8.sp,
                        color = Color.Black
                    )
                )
            }

    ) {

        elements.forEach {
            val details = it.draw()
            clipRect {
                withTransform({
                    if(it.p1 == null) return@withTransform
                    rotate(
                        degrees = it.rotationAngle,
                        pivot = it.p1!!.toOffset()
                    )
                    scale(it.zoom)
                }) {
                        drawElement(
                            details = details,
                            scope = this,
                            selectedElementIndex = selectedElementIndex
                        )

                }
            }

        }
        if(lastTouchPoint != null) {
            drawCircle(
                center = lastTouchPoint.toOffset(),
                color = Color.Black,
                radius = 8f
            )

        }
    }
}

private fun calculateSize(p1: DrawPoint, p2: DrawPoint): Size {
    val width = p2.x - p1.x
    val height = p2.y - p1.y
    return Size(
        width = width,
        height = height
    )
}


fun drawElement(
    details: DrawDetails,
    scope: DrawScope,
    selectedElementIndex: Int?
) {



    when(details) {
        is DrawDetails.Circle -> {


            scope.drawCircle(
                center = details.p1.toOffset(),
                radius = details.radius,
                color = details.color,
                style = Stroke(
                    width = 4f
                ),
            )

        }
        is DrawDetails.Line -> {
            scope.drawLine(
                color = details.color,
                start = details.p1.toOffset(),
                end = details.p2.toOffset(),
                strokeWidth = 4f
            )

        }
        is DrawDetails.Point -> {
            scope.drawCircle(
                center = details.p1.toOffset(),
                color = details.color,
                radius = 8f
            )
            Log.d("TAG", "onCreate: $details")
        }
        is DrawDetails.Rectangle -> {
            scope.drawRect(
                topLeft = details.p1.toOffset(),
                size = calculateSize(details.p1, details.p2),
                color = details.color,
                style = Stroke(
                    width = 4f
                )
            )
        }

        is DrawDetails.Image -> {
            scope.drawImage(
                image = details.bitmap.asImageBitmap(),
                topLeft = details.p1.toOffset(),

            )
        }
    }

}