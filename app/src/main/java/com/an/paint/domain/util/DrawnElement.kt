package com.an.paint.domain.util

sealed interface DrawnElement{
    object Line: DrawnElement
    object Circle: DrawnElement
    object Rectangle: DrawnElement
    object Point: DrawnElement
}