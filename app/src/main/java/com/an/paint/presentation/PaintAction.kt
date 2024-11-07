package com.an.paint.presentation

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.an.paint.domain.FilterType
import com.an.paint.domain.model.DrawDetails
import com.an.paint.domain.model.DrawPoint
import com.an.paint.domain.util.Element

sealed interface PaintAction {
    data class TapDrawingArea(val p1: DrawPoint): PaintAction
    data class SelectShape(val index: Int): PaintAction
    data class PickColor(val color: Color): PaintAction
    data class EditElement(val newElement: Element): PaintAction

    data class AddImage(val bitmap: Bitmap, val size: DrawPoint): PaintAction
    data class ApplyFilter(val filter: FilterType): PaintAction
    data class TransformElement(val zoom: Float, val rotation: Float, val offset: Offset): PaintAction
    object ChangeMode: PaintAction


}