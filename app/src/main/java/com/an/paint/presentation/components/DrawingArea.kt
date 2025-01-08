package com.an.paint.presentation.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.an.paint.domain.model.BezierCurve
import com.an.paint.domain.model.Circle
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
    selectedElement: Element? = null,
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
                    }
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
                    val alpha = if(selectedElementIndex == null) 1f else 0.6f

                    if(selectedElementIndex == index) return@withTransform

                    drawElement(
                        element = element,
                        scope = this,
                        alpha = alpha
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
        if(selectedElement != null) {
            clipRect {
                withTransform({
                    if(selectedElement.p1 == null) return@withTransform

                    rotate(
                        degrees = selectedElement.rotationAngle,
                        pivot = calculatePivot(selectedElement)
                    )

                    val scale = if(selectedElement is Image) selectedElement.zoom else 1f

                    scale(
                        scale = scale,
                        pivot = selectedElement.p1!!
                    )
                }) {
                    drawElement(
                        element = selectedElement,
                        scope = this,
                    )
                }
            }
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
    element: Element,
    scope: DrawScope,
    alpha: Float = 1f
) {


    when(element) {
        is Line -> {
            scope.drawLine(
                color = element.color,
                start = element.p1,
                end = element.end,
                strokeWidth = 4f,
                alpha = alpha
            )
        }
        is Circle -> {
            scope.drawCircle(
                center = element.p1,
                radius = element.radius,
                color = element.color,
                style = Stroke(
                    width = 4f
                ),
                alpha = alpha
            )
        }
        is Rectangle -> {
            scope.drawRect(
                topLeft = element.p1,
                size = calculateSize(element.p1, element.bottomRight),
                color = element.color,
                style = Stroke(
                    width = 4f
                ),
                alpha = alpha
            )
        }
        is Image -> {
            scope.drawImage(
                image = element.bitmap.asImageBitmap(),
                topLeft = element.p1,
                alpha = alpha
            )
        }
        is BezierCurve -> {

            scope.drawCircle(
                center = element.p1!!,
                color = Color.Black,
                radius = 8f
            )
            element.points.forEach {
                scope.drawCircle(
                    center = it,
                    color = Color.Black,
                    radius = 8f
                )
            }

            val path = Path().apply {
                moveTo(element.p1!!.x, element.p1!!.y)

                val points = element.points


                when(element.points.size) {
                    1 -> {
                        lineTo(points[0].x, points[0].y)

                    }
                    2 -> {
                        quadraticBezierTo(
                            x1 = points[0].x,
                            y1 = points[0].y,
                            x2 = points[1].x,
                            y2 = points[1].y
                        )
                    }
                    3 -> {
                        cubicTo(
                            x1 = points[0].x,
                            y1 = points[0].y,
                            x2 = points[1].x,
                            y2 = points[1].y,
                            x3 = points[2].x,
                            y3 = points[2].y
                        )
                    }
                }


            }

            scope.drawPath(
                path = path,
                color = element.color,
                style = Stroke(width = 5f)
            )
        }
    }

}