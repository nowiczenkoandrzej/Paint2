package com.an.paint.presentation.components.editing

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.an.paint.domain.FilterType
import com.an.paint.domain.model.BezierCurve
import com.an.paint.domain.model.Circle
import com.an.paint.domain.model.Image
import com.an.paint.domain.model.Line
import com.an.paint.domain.model.Rectangle
import com.an.paint.domain.model.Element

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedElementEditPanel(
    element: Element,
    modifier: Modifier = Modifier,
    onSubmit: () -> Unit,
    onChangeDetails: (Element) -> Unit,
    onApplyFilter: (FilterType) -> Unit,
    onCutImage: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Edit Element:",
            fontSize = 24.sp,
        )

        when(element) {
            is Circle -> {
                CircleEditPanel(
                    onChangeDetails = { onChangeDetails(it) },
                    editedCircle = element
                )

            }
            is Line -> {
                LineEditPanel(
                    onChangeDetails = { onChangeDetails(it) },
                    editedLine = element
                )
            }
            is Rectangle -> {
                RectangleEditPanel(
                    onChangeDetails = { onChangeDetails(it)},
                    editedRectangle = element
                )
            }
            is Image -> {
                ImageEditPanel(
                    onChangeDetails = { onApplyFilter(it) },
                    onCutImage = { onCutImage() }
                )
            }

            is BezierCurve -> {

            }
        }

        Button(
            onClick = { onSubmit() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Save Changes")
        }
    }

}