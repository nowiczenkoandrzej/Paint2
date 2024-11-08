package com.an.paint.domain.model

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap

sealed class DrawDetails(
    p1: Offset,
    p2: Offset? = null,
    radius: Float? = null,
    color: Color = Color.Black,
    bitmap: Bitmap? = null,
    rotationAngle: Float = 0f,
    zoom: Float = 1f
) {
    data class Line(
        val p1: Offset,
        val p2: Offset,
        val color: Color = Color.Black,
        val rotationAngle: Float = 0f,
        val zoom: Float = 1f
    ): DrawDetails(
        p1 = p1,
        p2 = p2,
        color = color,
        rotationAngle = rotationAngle,
        zoom = zoom
    )
    data class Rectangle(
        val p1: Offset,
        val p2: Offset,
        val color: Color = Color.Black,
        val rotationAngle: Float = 0f,
        val zoom: Float = 1f
    ): DrawDetails(
        p1 = p1,
        p2 = p2,
        color = color,
        rotationAngle = rotationAngle,
        zoom = zoom
    )
    data class Circle(
        val p1: Offset,
        val radius: Float,
        val color: Color = Color.Black,
        val rotationAngle: Float = 0f,
        val zoom: Float = 1f
    ): DrawDetails(
        p1 = p1,
        radius = radius,
        color = color,
        rotationAngle = rotationAngle,
        zoom = zoom
    )
    data class Point(
        val p1: Offset,
        val color: Color = Color.Black,
        val rotationAngle: Float = 0f,
        val zoom: Float = 1f
    ): DrawDetails(
        p1 = p1,
        color = color,
        rotationAngle = rotationAngle,
        zoom = zoom
    )

    data class Image(
        val p1: Offset,
        val p2: Offset,
        val bitmap: Bitmap,
        val rotationAngle: Float = 0f,
        val zoom: Float = 1f
    ): DrawDetails(
        p1 = p1,
        p2 = p2,
        bitmap = bitmap,
        rotationAngle = rotationAngle,
        zoom = zoom
    )


}
