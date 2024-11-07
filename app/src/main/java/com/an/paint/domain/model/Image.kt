package com.an.paint.domain.model

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.paint.domain.util.Element

data class Image(
    override val p1: DrawPoint,
    val bottomRight: DrawPoint,
    val bitmap: Bitmap,
    override val color: Color = Color.White,
    override val rotationAngle: Float = 0f,
    override val zoom: Float = 1f
): Element {
    override fun draw(): DrawDetails {
        return DrawDetails.Image(
            p1 = p1,
            p2 = bottomRight,
            bitmap = bitmap,
            zoom = zoom
        )
    }

    override fun containsTouchPoint(point: DrawPoint): Boolean {
        val xCorrect = (point.x > p1.x && point.x < bottomRight.x) ||
                (point.x > bottomRight.x && point.x < p1.x)

        val yCorrect = (point.y > bottomRight.y && point.y < p1.y) ||
                (point.y > p1.y && point.y < bottomRight.y)

        return xCorrect && yCorrect
    }


    override fun changeColor(color: Color): Element {
        return this
    }

    override fun transform(zoom: Float, rotation: Float, offset: Offset): Element {
        val width = (p1.x - bottomRight.x) * zoom
        val height = (p1.y - bottomRight.y) * zoom

        val newPoint = DrawPoint(
            x = p1.x + offset.x,
            y = p1.y + offset.y
        )

        var newZoom = this.zoom * zoom

        if (newZoom > 1f) newZoom = 1f

        return this.copy(
            rotationAngle = rotationAngle + rotation,
            p1 = newPoint,
            bottomRight = DrawPoint(
                x = newPoint.x + width,
                y = newPoint.y + height
            ),
            zoom = newZoom
        )
    }

}
