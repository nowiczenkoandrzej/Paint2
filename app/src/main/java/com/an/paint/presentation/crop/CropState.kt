package com.an.paint.presentation.crop

import androidx.compose.ui.geometry.Offset

data class CropState(
    val topLeft: Offset = Offset.Zero,
    val bottomRight: Offset? = null,

)
