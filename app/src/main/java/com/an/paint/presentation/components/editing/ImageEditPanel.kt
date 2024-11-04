package com.an.paint.presentation.components.editing

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.an.paint.domain.FilterType
import com.an.paint.domain.model.Image

@Composable
fun ImageEditPanel(
    onChangeDetails: (FilterType) -> Unit,
    modifier: Modifier = Modifier,
    editedImage: Image
)  {
    Row {
        Button(onClick = {
            onChangeDetails(FilterType.Smooth)
        }) {
            Text(text = "Smooth")
        }
        Button(onClick = {
            onChangeDetails(FilterType.Median)
        }) {
            Text(text = "Median")
        }
        Button(onClick = {
            onChangeDetails(FilterType.Sobel)
        }) {
            Text(text = "Sobel")
        }
        Button(onClick = {
            onChangeDetails(FilterType.Dilate)
        }) {
            Text(text = "Dilate")
        }
        Button(onClick = {
            onChangeDetails(FilterType.Erode)
        }) {
            Text(text = "Erode")
        }
    }
}