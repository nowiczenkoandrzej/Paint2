package com.an.paint.domain.model

import androidx.compose.ui.graphics.Color
import com.an.paint.domain.util.Element

class Rectangle(
    val topLeft: DrawPoint,
    val bottomRight: DrawPoint,
    override val color: Color
): Element {
    override fun draw(): DrawDetails {
        return DrawDetails.Rectangle(p1 = topLeft, p2 = bottomRight, color = color)
    }

    override fun containsTouchPoint(point: DrawPoint): Boolean {
        val xCorrect = (point.x > topLeft.x && point.x < bottomRight.x) ||
            (point.x > bottomRight.x && point.x < topLeft.x)

        val yCorrect = (point.y > bottomRight.y && point.y < topLeft.y) ||
                (point.y > topLeft.y && point.y < bottomRight.y)

        return xCorrect && yCorrect
    }
}