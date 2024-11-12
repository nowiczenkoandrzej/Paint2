package com.an.paint.domain.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.paint.domain.util.Element

data class BezierCurve(
    override val color: Color = Color.Black,
    override val rotationAngle: Float = 0f,
    override val p1: Offset?,
    override val zoom: Float = 1f,
    val p2: Offset,
    val p3: Offset,
    val p4: Offset
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

}