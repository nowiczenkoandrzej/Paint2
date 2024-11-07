package com.an.paint.domain.model

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.paint.domain.util.Element
import kotlin.math.pow
import kotlin.math.sqrt

data class Circle(
    override val p1: DrawPoint,
    val radius: Float,
    override val color: Color,
    override val rotationAngle: Float = 0f,
    override val zoom: Float = 1f
): Element {
    override fun draw(): DrawDetails {
        return DrawDetails.Circle(
            p1 = p1,
            radius = radius,
            color = color,
            zoom = zoom
        )
    }

    override fun containsTouchPoint(point: DrawPoint): Boolean {
        return calculateRadius(p1, point) <= radius
    }

    override fun changeColor(color: Color): Element {
        return this.copy(
            color = color
        )
    }

    override fun transform(zoom: Float, rotation: Float, offset: Offset): Element {
        return this.copy(
            p1 = DrawPoint(
                x = p1.x + offset.x,
                y = p1.y + offset.y
            ),
            radius = this.radius * zoom,
            rotationAngle = rotationAngle + rotation
        )
    }

    private fun calculateRadius(p1: DrawPoint, p2: DrawPoint): Float {
        return sqrt((p2.x - p1.x).pow(2) + (p2.y - p1.y).pow(2))
    }
}