package com.an.paint.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.paint.domain.model.DrawDetails
import com.an.paint.domain.model.DrawPoint
import com.an.paint.domain.util.Element

sealed interface PaintAction {
    data class TapDrawingArea(val p1: DrawPoint): PaintAction
    data class SelectShape(val index: Int): PaintAction
    data class DragElement(val offset: Offset): PaintAction
    data class PickColor(val color: Color): PaintAction
    data class EditElement(val newElement: Element): PaintAction
    object ChangeMode: PaintAction


}