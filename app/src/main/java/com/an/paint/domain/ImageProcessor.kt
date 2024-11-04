package com.an.paint.domain

import android.graphics.Bitmap

interface ImageProcessor {

    suspend fun smoothingFilter(source: Bitmap, kernelSize: Int = 3): Bitmap
    suspend fun medianFilter(source: Bitmap, kernelSize: Int = 3): Bitmap
    suspend fun sobelFilter(source: Bitmap): Bitmap
    suspend fun dilate(source: Bitmap, kernelSize: Int = 3): Bitmap
    suspend fun erode(source: Bitmap, kernelSize: Int = 3): Bitmap
}