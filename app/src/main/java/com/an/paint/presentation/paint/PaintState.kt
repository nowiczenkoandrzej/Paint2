package com.an.paint.presentation.paint

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.paint.domain.util.Element
import com.an.paint.domain.util.Shape

data class PaintState(
    val selectedShape: Shape = Shape.LINE,
    val helperPoint: Offset? = null,
    val elements: List<Element> = emptyList(),
    val selectedColor: Color = Color.Black,
    val isInEditMode: Boolean = false,
    val selectedElement: Element? = null,
    val selectedElementIndex: Int? = null
)
