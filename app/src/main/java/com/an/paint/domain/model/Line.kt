package com.an.paint.domain.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.paint.domain.util.Element
import kotlin.IllegalArgumentException
import kotlin.math.abs

data class Line(
    override val p1: DrawPoint,
    val end: DrawPoint,
    override val color: Color,
    override val rotationAngle: Float = 0f,
    override val zoom: Float = 1f
): Element {
    override fun draw(): DrawDetails {
        return DrawDetails.Line(
            p1 = p1,
            p2 = end,
            color = color,
            zoom = zoom
        )
    }

    override fun containsTouchPoint(point: DrawPoint): Boolean {
        try {
            val (a, b) = linearFunction(p1, end)

            if(!point.isXBetween(p1, end))
                return false

            val tolerance = 20f

            return abs(point.y - (a * point.x + b)) <= tolerance

        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return false
        }
    }

    override fun changeColor(color: Color): Element {
        return this.copy(
            color = color
        )
    }

    override fun transform(zoom: Float, rotation: Float, offset: Offset): Element {
        val width = abs(p1.x - end.x) * zoom
        val height = abs(p1.y - end.y) * zoom

        val newPoint = DrawPoint(
            x = p1.x + offset.x,
            y = p1.y + offset.y
        )

        return this.copy(
            rotationAngle = rotationAngle + rotation,
            p1 = newPoint,
            end = DrawPoint(
                x = newPoint.x + width,
                y = newPoint.y + height
            )
        )
    }

    private fun linearFunction(p1: DrawPoint, p2: DrawPoint): Pair<Float, Float> {
        if(p1.x == p2.x) throw IllegalArgumentException("Same x")

        val a = (p2.y - p1.y) / (p2.x - p1.x)
        val b = p1.y - a * p1.x


        return Pair(a, b)
    }


}
