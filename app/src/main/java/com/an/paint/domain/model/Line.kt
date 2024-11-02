package com.an.paint.domain.model

import androidx.compose.ui.graphics.Color
import com.an.paint.domain.util.Element
import kotlin.IllegalArgumentException
import kotlin.math.abs

class Line(
    val start: DrawPoint,
    val end: DrawPoint,
    override val color: Color
): Element {
    override fun draw(): DrawDetails {
        return DrawDetails.Line(p1 = start, p2 = end, color = color)
    }

    override fun containsTouchPoint(point: DrawPoint): Boolean {
        try {
            val (a, b) = linearFunction(start, end)

            if(!point.isXBetween(start, end))
                return false

            val tolerance = 20f

            return abs(point.y - (a * point.x + b)) <= tolerance

        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            return false
        }
    }

    private fun linearFunction(p1: DrawPoint, p2: DrawPoint): Pair<Float, Float> {
        if(p1.x == p2.x) throw IllegalArgumentException("Same x")

        val a = (p2.y - p1.y) / (p2.x - p1.x)
        val b = p1.y - a * p1.x


        return Pair(a, b)
    }


}
