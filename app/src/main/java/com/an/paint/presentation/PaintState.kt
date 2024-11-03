package com.an.paint.presentation

import androidx.compose.ui.graphics.Color
import com.an.paint.domain.model.DrawDetails
import com.an.paint.domain.model.DrawPoint
import com.an.paint.domain.util.Element

data class PaintState(
    val selectedShape: Int = 1,
    val helperPoint: DrawPoint? = null,
    val elements: List<Element> = emptyList(),
    val selectedColor: Color = Color.Black,
    val isInEditMode: Boolean = false,
    val selectedElement: Element? = null,
    val selectedElementIndex: Int? = null
)
