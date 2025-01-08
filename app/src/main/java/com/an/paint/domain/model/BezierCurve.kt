package com.an.paint.domain.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.paint.domain.util.Element

data class BezierCurve(
    override val color: Color = Color.Black,
    override val rotationAngle: Float = 0f,
    override val p1: Offset?,
    override val zoom: Float = 1f,
    val points: List<Offset> = emptyList()
): Element {


    override fun containsTouchPoint(point: Offset): Boolean {


        return false
    }

    override fun changeColor(color: Color): Element {
        return this.copy(
            color = color
        )
    }

    override fun transform(zoom: Float, rotation: Float, offset: Offset): Element {
        return this.copy(
            zoom = zoom,
            rotationAngle = rotation,
            p1 = offset
        )
    }

    fun modifyCurve(point: Int, offset: Offset): Element {
        val start = if(point != 0) {
            this.p1
        } else {
            Offset(
                x = this.p1!!.x + offset.x,
                y = this.p1.y + offset.y
            )
        }

        val points = this.points

        val newPoints = points.toMutableList().apply {
            set(
                point,
                Offset(
                    x = points[point].x + offset.x,
                    y = points[point].y + offset.y,
                )
            )
        }.toList()

        return this.copy(
            p1 = start,
            points = newPoints
        )
    }

}