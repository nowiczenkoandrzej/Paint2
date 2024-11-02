package com.an.paint.presentation.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
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
    onDrag: (Offset) -> Unit,
    elements: List<Element> = emptyList(),
    lastTouchPoint: DrawPoint?,
    dragPaintElement: DrawDetails?,
) {
    val textMeasurer = rememberTextMeasurer()


    Canvas(
        modifier = modifier
            .padding(4.dp)
            .fillMaxSize()
            .pointerInput(Unit) {

                detectTapGestures(
                    onTap = { tapOffset ->
                        onTap(tapOffset)
                    },
                )
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    onDrag(dragAmount)

                    Log.d("TAG", "DrawingArea: ${dragAmount.x} -- ${dragAmount.y}")
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
            drawElement(
                details = details,
                scope = this
            )

        }
        if(lastTouchPoint != null) {
            drawCircle(
                center = lastTouchPoint.toOffset(),
                color = Color.Black,
                radius = 8f
            )
        }
        if(dragPaintElement != null) {
            drawElement(
                details = dragPaintElement,
                scope = this
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
    scope: DrawScope
) {
    when(details) {
        is DrawDetails.Circle -> {
            scope.clipRect {
                drawCircle(
                    center = details.p1.toOffset(),
                    radius = details.radius,
                    color = details.color,
                    style = Stroke(
                        width = 4f
                    )
                )
            }
        }
        is DrawDetails.Line -> {
            scope.clipRect {
                drawLine(
                    color = details.color,
                    start = details.p1.toOffset(),
                    end = details.p2.toOffset(),
                    strokeWidth = 4f
                )
            }
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
            scope.clipRect {
                drawRect(
                    topLeft = details.p1.toOffset(),
                    size = calculateSize(details.p1, details.p2),
                    color = details.color,
                    style = Stroke(
                        width = 4f
                    )
                )
            }
        }
    }

}