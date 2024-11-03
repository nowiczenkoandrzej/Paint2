package com.an.paint.domain.model

import androidx.compose.ui.graphics.Color
import com.an.paint.domain.util.Element
import kotlin.math.pow
import kotlin.math.sqrt

class Circle(
    val center: DrawPoint,
    val radius: Float,
    override val color: Color,
): Element {
    override fun draw(): DrawDetails {
        return DrawDetails.Circle(p1 = center, radius = radius, color = color)
    }

    override fun containsTouchPoint(point: DrawPoint): Boolean {
        return calculateRadius(center, point) <= radius
    }

    private fun calculateRadius(p1: DrawPoint, p2: DrawPoint): Float {
        return sqrt((p2.x - p1.x).pow(2) + (p2.y - p1.y).pow(2))
    }
}