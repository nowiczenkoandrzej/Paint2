package com.an.paint.domain.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.paint.domain.model.DrawDetails
import com.an.paint.domain.model.DrawPoint

interface Element {

    val color: Color

    fun draw(): DrawDetails

    fun containsTouchPoint(point: DrawPoint): Boolean
    fun move(offset: Offset): Element
}