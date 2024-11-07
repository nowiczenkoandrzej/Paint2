package com.an.paint.domain.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.paint.domain.model.DrawDetails
import com.an.paint.domain.model.DrawPoint

interface Element {

    val color: Color
    val rotationAngle: Float
    val p1: DrawPoint?
    val zoom: Float

    fun draw(): DrawDetails
    fun containsTouchPoint(point: DrawPoint): Boolean
    fun changeColor(color: Color): Element
    fun transform(zoom: Float, rotation: Float, offset: Offset): Element
}