package com.an.paint.domain.model

import androidx.compose.ui.graphics.Color
import com.an.paint.domain.util.DrawnElement

sealed class DrawDetails(
    category: DrawnElement,
    p1: DrawPoint,
    p2: DrawPoint? = null,
    radius: Float? = null,
    color: Color = Color.Black
) {
    data class Line(
        val p1: DrawPoint,
        val p2: DrawPoint,
        val color: Color = Color.Black
    ): DrawDetails(
        category = DrawnElement.Line,
        p1 = p1,
        p2 = p2,
        color = color
    )
    data class Rectangle(
        val p1: DrawPoint,
        val p2: DrawPoint,
        val color: Color = Color.Black
    ): DrawDetails(
        category = DrawnElement.Rectangle,
        p1 = p1,
        p2 = p2,
        color = color
    )
    data class Circle(
        val p1: DrawPoint,
        val radius: Float,
        val color: Color = Color.Black
    ): DrawDetails(
        category = DrawnElement.Circle,
        p1 = p1,
        radius = radius,
        color = color
    )
    data class Point(
        val p1: DrawPoint,
        val color: Color = Color.Black
    ): DrawDetails(
        category = DrawnElement.Point,
        p1 = p1,
        color = color
    )


}
