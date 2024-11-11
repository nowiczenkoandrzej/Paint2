package com.an.paint.presentation.crop

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.an.paint.domain.model.Image
import com.an.paint.presentation.PaintViewModel
import com.an.paint.presentation.paint.PaintAction
import java.lang.Float.min
import kotlin.math.max

@Composable
fun ImageCropperScreen(
    viewModel: PaintViewModel,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .background(Color.Black)
    ) {

        val paintState = viewModel
            .paintState
            .collectAsState()
            .value

        Log.d("TAG", "ImageCropperScreen: ${paintState.selectedElement}")



        val cropState = viewModel
            .cropState
            .collectAsState()
            .value

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.White)
        ){
            val originalBitmap = (paintState.selectedElement as Image).originalBitmap

            val canvasWidth = size.width

            val canvasCenter = Offset(canvasWidth / 2f, canvasWidth / 2f)


            val bitmapWidth = originalBitmap.width.toFloat()
            val bitmapHeight = originalBitmap.height.toFloat()

            val scaleX = canvasWidth / bitmapWidth
            val scaleY = canvasWidth / bitmapHeight
            val scale = min(scaleX, scaleY)


            clipRect {
                withTransform({

                    translate(
                        left = canvasCenter.x,
                        top = canvasCenter.y
                    )
                    scale(
                        scale = scale,
                        pivot = Offset.Zero
                    )
                    translate(
                        left = -bitmapWidth / 2f,
                        top = -bitmapHeight / 2f
                    )

                }) {

                    drawImage(
                        image = originalBitmap.asImageBitmap(),
                        topLeft = Offset.Zero,
                    )
                }
            }
        }



    }

}