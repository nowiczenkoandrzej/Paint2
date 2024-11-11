package com.an.paint.presentation.components.editing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.an.paint.R
import com.an.paint.domain.FilterType
import com.an.paint.domain.model.Image

@Composable
fun ImageEditPanel(
    onChangeDetails: (FilterType) -> Unit,
    modifier: Modifier = Modifier,
    onCutImage: () -> Unit,
)  {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        LazyRow() {
            item {
                Button(onClick = {
                    onChangeDetails(FilterType.Smooth)
                }) {
                    Text(text = "Smooth")
                }
            }
            item {
                Button(onClick = {
                    onChangeDetails(FilterType.Median)
                }) {
                    Text(text = "Median")
                }
            }
            item {
                Button(onClick = {
                    onChangeDetails(FilterType.Sobel)
                }) {
                    Text(text = "Sobel")
                }
            }
            item {
                Button(onClick = {
                    onChangeDetails(FilterType.Dilate)
                }) {
                    Text(text = "Dilate")
                }
            }
            item {
                Button(onClick = {
                    onChangeDetails(FilterType.Erode)
                }) {
                    Text(text = "Erode")
                }
            }
        }

        IconButton(onClick = { onCutImage() }) {
            Icon(
                painter = painterResource(id = R.drawable.outline_content_cut_24),
                contentDescription = "Cut Image"
            )
        }
    }
}