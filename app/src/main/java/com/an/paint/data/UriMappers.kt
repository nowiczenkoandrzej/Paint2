package com.an.paint.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

fun Uri.toBitmap(context: Context): Bitmap {
    val inputStream = context.contentResolver.openInputStream(this)
    return BitmapFactory.decodeStream(inputStream)
}