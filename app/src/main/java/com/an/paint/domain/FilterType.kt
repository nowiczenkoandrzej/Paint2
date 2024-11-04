package com.an.paint.domain

sealed interface FilterType{
    object Smooth: FilterType
    object Median: FilterType
    object Sobel: FilterType
    object Dilate: FilterType
    object Erode: FilterType
}