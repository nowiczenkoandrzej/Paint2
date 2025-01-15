package com.an.paint.domain.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

interface Element {

    val color: Color
    val rotationAngle: Float
    val p1: Offset?
    val zoom: Float

    fun containsTouchPoint(point: Offset): Boolean
    fun changeColor(color: Color): Element
    fun transform(zoom: Float, rotation: Float, offset: Offset, centroid: Offset): Element
}