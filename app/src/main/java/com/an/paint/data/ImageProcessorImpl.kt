package com.an.paint.data

import android.graphics.Bitmap
import android.graphics.Color
import com.an.paint.domain.ImageProcessor
import java.lang.Math.sqrt
import kotlin.math.pow

class ImageProcessorImpl: ImageProcessor {
    override suspend fun smoothingFilter(source: Bitmap, kernelSize: Int): Bitmap {
        val result = Bitmap.createBitmap(source.width, source.height, source.config)
        val pixels = IntArray(source.width * source.height)
        source.getPixels(pixels, 0, source.width, 0, 0, source.width, source.height)

        for (y in 0 until source.height) {
            for (x in 0 until source.width) {
                var sumR = 0
                var sumG = 0
                var sumB = 0
                var count = 0

                for (ky in -kernelSize/2..kernelSize/2) {
                    for (kx in -kernelSize/2..kernelSize/2) {
                        val px = x + kx
                        val py = y + ky

                        if (px >= 0 && px < source.width && py >= 0 && py < source.height) {
                            val color = pixels[py * source.width + px]
                            sumR += Color.red(color)
                            sumG += Color.green(color)
                            sumB += Color.blue(color)
                            count++
                        }
                    }
                }

                result.setPixel(x, y, Color.rgb(
                    sumR / count,
                    sumG / count,
                    sumB / count
                ))
            }
        }
        return result
    }

    override suspend fun medianFilter(source: Bitmap, kernelSize: Int): Bitmap {
        val result = Bitmap.createBitmap(source.width, source.height, source.config)
        val pixels = IntArray(source.width * source.height)
        source.getPixels(pixels, 0, source.width, 0, 0, source.width, source.height)

        for (y in 0 until source.height) {
            for (x in 0 until source.width) {
                val valuesR = mutableListOf<Int>()
                val valuesG = mutableListOf<Int>()
                val valuesB = mutableListOf<Int>()

                for (ky in -kernelSize/2..kernelSize/2) {
                    for (kx in -kernelSize/2..kernelSize/2) {
                        val px = x + kx
                        val py = y + ky

                        if (px >= 0 && px < source.width && py >= 0 && py < source.height) {
                            val color = pixels[py * source.width + px]
                            valuesR.add(Color.red(color))
                            valuesG.add(Color.green(color))
                            valuesB.add(Color.blue(color))
                        }
                    }
                }

                valuesR.sort()
                valuesG.sort()
                valuesB.sort()

                val medianIndex = valuesR.size / 2
                result.setPixel(x, y, Color.rgb(
                    valuesR[medianIndex],
                    valuesG[medianIndex],
                    valuesB[medianIndex]
                ))
            }
        }
        return result
    }

    override suspend fun sobelFilter(source: Bitmap): Bitmap {
        val result = Bitmap.createBitmap(source.width, source.height, source.config)
        val pixels = IntArray(source.width * source.height)
        source.getPixels(pixels, 0, source.width, 0, 0, source.width, source.height)

        val sobelX = arrayOf(
            intArrayOf(-1, 0, 1),
            intArrayOf(-2, 0, 2),
            intArrayOf(-1, 0, 1)
        )

        val sobelY = arrayOf(
            intArrayOf(-1, -2, -1),
            intArrayOf(0, 0, 0),
            intArrayOf(1, 2, 1)
        )

        for (y in 1 until source.height - 1) {
            for (x in 1 until source.width - 1) {
                var pixelX = 0
                var pixelY = 0

                for (i in -1..1) {
                    for (j in -1..1) {
                        val pixel = pixels[(y + i) * source.width + (x + j)]
                        val gray = (Color.red(pixel) + Color.green(pixel) + Color.blue(pixel)) / 3
                        pixelX += gray * sobelX[i + 1][j + 1]
                        pixelY += gray * sobelY[i + 1][j + 1]
                    }
                }

                val magnitude = sqrt(pixelX.toDouble().pow(2) + pixelY.toDouble().pow(2)).toInt()
                val edge = magnitude.coerceIn(0, 255)
                result.setPixel(x, y, Color.rgb(edge, edge, edge))
            }
        }
        return result
    }

    override suspend fun dilate(source: Bitmap, kernelSize: Int): Bitmap {
        val result = Bitmap.createBitmap(source.width, source.height, source.config)
        val pixels = IntArray(source.width * source.height)
        source.getPixels(pixels, 0, source.width, 0, 0, source.width, source.height)

        for (y in 0 until source.height) {
            for (x in 0 until source.width) {
                var maxR = 0
                var maxG = 0
                var maxB = 0

                for (ky in -kernelSize/2..kernelSize/2) {
                    for (kx in -kernelSize/2..kernelSize/2) {
                        val px = x + kx
                        val py = y + ky

                        if (px >= 0 && px < source.width && py >= 0 && py < source.height) {
                            val color = pixels[py * source.width + px]
                            maxR = maxOf(maxR, Color.red(color))
                            maxG = maxOf(maxG, Color.green(color))
                            maxB = maxOf(maxB, Color.blue(color))
                        }
                    }
                }

                result.setPixel(x, y, Color.rgb(maxR, maxG, maxB))
            }
        }
        return result
    }

    override suspend fun erode(source: Bitmap, kernelSize: Int): Bitmap {
        val result = Bitmap.createBitmap(source.width, source.height, source.config)
        val pixels = IntArray(source.width * source.height)
        source.getPixels(pixels, 0, source.width, 0, 0, source.width, source.height)

        for (y in 0 until source.height) {
            for (x in 0 until source.width) {
                var minR = 255
                var minG = 255
                var minB = 255

                for (ky in -kernelSize/2..kernelSize/2) {
                    for (kx in -kernelSize/2..kernelSize/2) {
                        val px = x + kx
                        val py = y + ky

                        if (px >= 0 && px < source.width && py >= 0 && py < source.height) {
                            val color = pixels[py * source.width + px]
                            minR = minOf(minR, Color.red(color))
                            minG = minOf(minG, Color.green(color))
                            minB = minOf(minB, Color.blue(color))
                        }
                    }
                }

                result.setPixel(x, y, Color.rgb(minR, minG, minB))
            }
        }
        return result
    }
}