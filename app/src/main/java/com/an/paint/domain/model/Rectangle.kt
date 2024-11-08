package com.an.paint.domain.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.paint.domain.util.Element
import java.lang.Math.abs

data class Rectangle(
    override val p1: Offset,
    val bottomRight: Offset,
    override val color: Color,
    override val rotationAngle: Float = 0f,
    override val zoom: Float = 1f
): Element {
    override fun draw(): DrawDetails {
        return DrawDetails.Rectangle(
            p1 = p1,
            p2 = bottomRight,
            color = color,
            zoom = zoom
        )
    }

    override fun containsTouchPoint(point: Offset): Boolean {
        val xCorrect = (point.x > p1.x && point.x < bottomRight.x) ||
            (point.x > bottomRight.x && point.x < p1.x)

        val yCorrect = (point.y > bottomRight.y && point.y < p1.y) ||
                (point.y > p1.y && point.y < bottomRight.y)

        return xCorrect && yCorrect
    }

    override fun changeColor(color: Color): Element {
        return this.copy(
            color = color
        )
    }

    override fun transform(zoom: Float, rotation: Float, offset: Offset): Element {
        val width = abs(p1.x - bottomRight.x) * zoom
        val height = abs(p1.y - bottomRight.y) * zoom

        val newPoint = Offset(
            x = p1.x + offset.x,
            y = p1.y + offset.y
        )

        return this.copy(
            rotationAngle = rotationAngle + rotation,
            p1 = newPoint,
            bottomRight = Offset(
                x = newPoint.x + width,
                y = newPoint.y + height
            )
        )
    }
}