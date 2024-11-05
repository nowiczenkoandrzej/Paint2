package com.an.paint.domain.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.paint.domain.util.Element

class DrawPoint(
    val x: Float,
    val y: Float,
    override val color: Color = Color.Black
): Element {
    override fun draw(): DrawDetails {
        return DrawDetails.Point(p1 = DrawPoint(x = x, y = y, color = color))
    }

    override fun containsTouchPoint(point: DrawPoint): Boolean {
        return false
    }

    override fun move(offset: Offset): Element {
        return DrawPoint(
            x = this.x + offset.x,
            y = this.y + offset.y
        )
    }

    fun isXBetween(p1: DrawPoint, p2: DrawPoint): Boolean {
        return (p1.x < x && p2.x > x) ||
                (p2.x < x && p1.x > x)
    }
}

fun DrawPoint.toOffset(): Offset {
    return Offset(
        x = x,
        y = y
    )
}

fun Offset.toDrawPoint(): DrawPoint {
    return DrawPoint(
        x = x,
        y = y,
    )
}