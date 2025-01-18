package com.an.paint.presentation.paint

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.paint.domain.FilterType
import com.an.paint.domain.model.Element
import com.an.paint.domain.util.Shape

sealed interface PaintAction {
    data class TapDrawingArea(val p1: Offset): PaintAction
    data class SelectShape(val shape: Shape): PaintAction
    data class PickColor(val color: Color): PaintAction
    data class EditElement(val newElement: Element): PaintAction
    data class AddImage(val bitmap: Bitmap, val size: Offset): PaintAction
    data class ApplyFilter(val filter: FilterType): PaintAction
    data class TransformElement(val zoom: Float, val rotation: Float, val offset: Offset, val centroid: Offset): PaintAction
    data class CutImage(val topLeft: Offset, val bottomRight: Offset): PaintAction
    data class SetCuttingFrame(val topLeft: Offset, val bottomRight: Offset): PaintAction
    object PickImageToCut: PaintAction
    object ChangeMode: PaintAction
    object SaveChanges: PaintAction
    object SaveToSavedState: PaintAction
    object LoadFromSavedState: PaintAction

}