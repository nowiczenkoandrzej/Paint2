package com.an.paint.domain.model

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import com.an.paint.domain.util.Element

data class Image(
    val topLeft: DrawPoint,
    val bottomRight: DrawPoint,
    val bitmap: Bitmap,
    override val color: Color = Color.White
): Element {
    override fun draw(): DrawDetails {
        return DrawDetails.Image(
            p1 = topLeft,
            p2 = bottomRight,
            bitmap = bitmap
        )
    }

    override fun containsTouchPoint(point: DrawPoint): Boolean {
        val xCorrect = (point.x > topLeft.x && point.x < bottomRight.x) ||
                (point.x > bottomRight.x && point.x < topLeft.x)

        val yCorrect = (point.y > bottomRight.y && point.y < topLeft.y) ||
                (point.y > topLeft.y && point.y < bottomRight.y)

        return xCorrect && yCorrect
    }

    override fun move(offset: Offset): Element {
        return Image(
            topLeft = this.topLeft.move(offset) as DrawPoint,
            bottomRight = this.topLeft.move(offset) as DrawPoint,
            bitmap = this.bitmap
        )
    }

}
