package com.an.paint.domain.model

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color


data class Image(
    override val p1: Offset,
    val bottomRight: Offset,
    val bitmap: Bitmap,
    val originalBitmap: Bitmap,
    override val color: Color = Color.White,
    override val rotationAngle: Float = 0f,
    override val zoom: Float = 1f
): Element {

    override fun containsTouchPoint(point: Offset): Boolean {
        val xCorrect = (point.x > p1.x && point.x < bottomRight.x) ||
                (point.x > bottomRight.x && point.x < p1.x)

        val yCorrect = (point.y > bottomRight.y && point.y < p1.y) ||
                (point.y > p1.y && point.y < bottomRight.y)

        return xCorrect && yCorrect
    }


    override fun changeColor(color: Color): Element {
        return this
    }

    override fun transform(
        zoom: Float,
        rotation: Float,
        offset: Offset,
        centroid: Offset
    ): Element {

        val center = Offset(
            x = (p1.x + bottomRight.x) / 2f,
            y = (p1.y + bottomRight.y) / 2f
        )

        var newZoom = this.zoom * zoom
        newZoom = newZoom.coerceIn(0.1f, 3f)

        val halfWidth = (bottomRight.x - p1.x) / 2f
        val halfHeight = (bottomRight.y - p1.y) / 2f

        val newHalfWidth = halfWidth * zoom
        val newHalfHeight = halfHeight * zoom

        val newP1 = Offset(
            x = center.x - newHalfWidth + offset.x,
            y = center.y - newHalfHeight + offset.y
        )

        val newBottomRight = Offset(
            x = center.x + newHalfWidth + offset.x,
            y = center.y + newHalfHeight + offset.y
        )

        return this.copy(
            rotationAngle = rotationAngle + rotation,
            p1 = newP1,
            bottomRight = newBottomRight,
            zoom = newZoom
        )
    }

}
