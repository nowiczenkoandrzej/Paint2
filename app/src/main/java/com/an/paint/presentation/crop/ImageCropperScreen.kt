package com.an.paint.presentation.crop

import android.util.Log
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.an.paint.domain.model.Image
import com.an.paint.presentation.PaintViewModel
import com.an.paint.presentation.paint.PaintAction
import java.lang.Float.min

@Composable
fun ImageCropperScreen(
    viewModel: PaintViewModel,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {

        val paintState = viewModel
            .paintState
            .collectAsState()
            .value

        /*val width = (paintState.selectedElement as Image)
            .originalBitmap.width

        val height = (paintState.selectedElement as Image)
            .originalBitmap.height

        LaunchedEffect(Unit){
            viewModel.onAction(PaintAction.SetCuttingFrame(
                topLeft = Offset.Zero,
                bottomRight = Offset(
                    x = width.toFloat(),
                    y = height.toFloat()
                )
            ))
        }*/

        Log.d("TAG", "ImageCropperScreen: ${paintState.selectedElement}")



        val cropState = viewModel
            .cropState
            .collectAsState()
            .value

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ){
            val originalBitmap = (paintState.selectedElement as Image).originalBitmap

            val canvasWidth = size.width
            val canvasHeight = size.height

            val bitmapWidth = originalBitmap.width.toFloat()
            val bitmapHeight = originalBitmap.height.toFloat()

            val scaleX = canvasWidth / bitmapWidth
            val scaleY = canvasHeight / bitmapHeight
            val scale = min(scaleX, scaleY)

            val translationX = (canvasWidth - bitmapWidth * scale) / 2
            val translationY = (canvasHeight - bitmapHeight * scale) / 2


            Log.d("TAG", "ImageCropperScreen canvas: $originalBitmap")
            Log.d("TAG", "ImageCropperScreen canvas: $translationY, $translationX")
            Log.d("TAG", "ImageCropperScreen canvas: $scale")


            withTransform({
                translate(-translationX, -translationY)
                scale(scale, scale)

            }) {

                drawImage(
                    image = originalBitmap.asImageBitmap(),
                    topLeft = Offset.Zero,
                    alpha = 1f
                )
            }
        }



    }

}