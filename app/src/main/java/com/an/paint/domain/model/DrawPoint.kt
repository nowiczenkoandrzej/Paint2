package com.an.paint.domain.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.paint.domain.util.Element

data class DrawPoint(
    val x: Float,
    val y: Float,
    override val color: Color = Color.Black,
    override val rotationAngle: Float = 0f,
    override val zoom: Float = 1f
): Element {
    override val p1: DrawPoint? = null
    override fun draw(): DrawDetails {
        return DrawDetails.Point(p1 = DrawPoint(x = x, y = y, color = color))
    }

    override fun containsTouchPoint(point: DrawPoint): Boolean {
        return false
    }

    override fun changeColor(color: Color): Element {
        return this
    }

    override fun transform(zoom: Float, rotation: Float, offset: Offset): Element {
        return this.copy(
            x = x + offset.x,
            y = y + offset.y
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