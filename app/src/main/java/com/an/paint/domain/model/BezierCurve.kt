package com.an.paint.domain.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.math.pow
import kotlin.math.sqrt


data class BezierCurve(
    override val color: Color = Color.Black,
    override val rotationAngle: Float = 0f,
    override val p1: Offset?,
    override val zoom: Float = 1f,
    val points: List<Offset> = emptyList()
): Element {


    override fun containsTouchPoint(point: Offset): Boolean {

        points.forEach {
            if(calculateDistance(point, it) > 50f) {
                return true
            }
        }

        return false
    }

    override fun changeColor(color: Color): Element {
        return this.copy(
            color = color
        )
    }

    override fun transform(
        zoom: Float,
        rotation: Float,
        offset: Offset,
        centroid: Offset
    ): Element {

        var minDistance = calculateDistance(p1!!, centroid)
        var minIndex: Int? = null

        points.forEachIndexed { index, it ->
            val distance = calculateDistance(it, centroid)
            if (distance < minDistance) {
                minDistance = distance
                minIndex = index
            }
        }

        val newPoint = if(minIndex == null) {
            Offset(
                x = p1.x + offset.x,
                y = p1.y + offset.y
            )
        }else {
            Offset(
                x = points[minIndex!!].x + offset.x,
                y = points[minIndex!!].y + offset.y
            )
        }

        if(minIndex == null) {
            return this.copy(
                p1 = newPoint
            )
        } else {

            val newList = points.toMutableList().apply {
                set(minIndex!!, newPoint)
            }.toList()

            return this.copy(
                points = newList
            )
        }

    }

    private fun calculateDistance(p1: Offset, p2: Offset): Float {
        val x = p1.x - p2.x
        val y = p1.y - p2.y

        return sqrt(x.pow(2) + y.pow(2))
    }


}