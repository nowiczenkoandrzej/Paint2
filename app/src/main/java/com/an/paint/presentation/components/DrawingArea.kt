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
import com.an.paint.domain.model.Circle
import com.an.paint.domain.model.DrawDetails
import com.an.paint.domain.model.Image
import com.an.paint.domain.model.Line
import com.an.paint.domain.model.Rectangle
import com.an.paint.domain.util.Element
import kotlin.math.abs
import kotlin.math.roundToInt


@Composable
fun DrawingArea(
    modifier: Modifier = Modifier,
    onTap: (Offset) -> Unit,
    elements: List<Element> = emptyList(),
    lastTouchPoint: Offset?,
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
                    Log.d(
                        "TAG",
                        "DrawingArea: zoom: $zoom, rotation: $rotation, centroid: $centroid, pan: $pan"
                    )
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

        elements.forEachIndexed { index, element ->
            val details = element.draw()
            clipRect {
                withTransform({
                    if(element.p1 == null) return@withTransform

                    rotate(
                        degrees = element.rotationAngle,
                        pivot = calculatePivot(element)
                    )

                    val scale = if(element is Image) element.zoom else 1f

                    scale(
                        scale = scale,
                        pivot = element.p1!!
                    )
                }) {
                        drawElement(
                            details = details,
                            scope = this,
                            selectedElementIndex = selectedElementIndex,
                            currentElementIndex = index
                        )

                }
            }

        }
        if(lastTouchPoint != null) {
            drawCircle(
                center = lastTouchPoint,
                color = Color.Black,
                radius = 8f
            )

        }
    }
}

private fun calculateSize(p1: Offset, p2: Offset): Size {
    val width = p2.x - p1.x
    val height = p2.y - p1.y
    return Size(
        width = width,
        height = height
    )
}

private fun calculatePivot(element: Element): Offset {
    return when(element) {
        is Image -> {
            val height = abs(element.bottomRight.y - element.p1.y)
            val width = abs(element.bottomRight.x - element.p1.x)

            Offset(
                x = width / 2 + element.p1.x,
                y = height / 2 + element.p1.y
            )
        }
        is Rectangle -> {
            val height = abs(element.bottomRight.y - element.p1.y) * element.zoom
            val width = abs(element.bottomRight.x - element.p1.x) * element.zoom

            Offset(
                x = width / 2 + element.p1.x,
                y = height / 2 + element.p1.y
            )
        }
        is Circle -> {
            element.p1
        }
        is Line -> {
            val height = abs(element.end.y - element.p1.y) * element.zoom
            val width = abs(element.end.x - element.p1.x) * element.zoom

            Offset(
                x = width / 2 + element.p1.x,
                y = height / 2 + element.p1.y
            )

        }

        else -> {
            Offset.Zero
        }
    }
}


private fun drawElement(
    details: DrawDetails,
    scope: DrawScope,
    selectedElementIndex: Int?,
    currentElementIndex: Int
) {

    val alpha = if(selectedElementIndex != null && selectedElementIndex != currentElementIndex) 0.6f else  1f

    Log.d("TAG", "drawElement: $selectedElementIndex")

    when(details) {
        is DrawDetails.Circle -> {



            scope.drawCircle(
                center = details.p1,
                radius = details.radius,
                color = details.color,
                style = Stroke(
                    width = 4f
                ),
                alpha = alpha
            )

        }
        is DrawDetails.Line -> {
            scope.drawLine(
                color = details.color,
                start = details.p1,
                end = details.p2,
                strokeWidth = 4f,
                alpha = alpha
            )

        }
        is DrawDetails.Point -> {
            scope.drawCircle(
                center = details.p1,
                color = details.color,
                radius = 8f,
                alpha = alpha
            )
            Log.d("TAG", "onCreate: $details")
        }
        is DrawDetails.Rectangle -> {
            scope.drawRect(
                topLeft = details.p1,
                size = calculateSize(details.p1, details.p2),
                color = details.color,
                style = Stroke(
                    width = 4f
                ),
                alpha = alpha
            )
        }

        is DrawDetails.Image -> {
            scope.drawImage(
                image = details.bitmap.asImageBitmap(),
                topLeft = details.p1,
                alpha = alpha
            )
        }
    }

}