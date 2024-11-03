package com.an.paint.domain.model

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap

sealed class DrawDetails(
    p1: DrawPoint,
    p2: DrawPoint? = null,
    radius: Float? = null,
    color: Color = Color.Black,
    bitmap: Bitmap? = null
) {
    data class Line(
        val p1: DrawPoint,
        val p2: DrawPoint,
        val color: Color = Color.Black
    ): DrawDetails(
        p1 = p1,
        p2 = p2,
        color = color
    )
    data class Rectangle(
        val p1: DrawPoint,
        val p2: DrawPoint,
        val color: Color = Color.Black
    ): DrawDetails(
        p1 = p1,
        p2 = p2,
        color = color
    )
    data class Circle(
        val p1: DrawPoint,
        val radius: Float,
        val color: Color = Color.Black
    ): DrawDetails(
        p1 = p1,
        radius = radius,
        color = color
    )
    data class Point(
        val p1: DrawPoint,
        val color: Color = Color.Black
    ): DrawDetails(
        p1 = p1,
        color = color
    )

    data class Image(
        val p1: DrawPoint,
        val p2: DrawPoint,
        val bitmap: Bitmap
    ): DrawDetails(
        p1 = p1,
        p2 = p2,
        bitmap = bitmap
    )


}
