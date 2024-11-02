package com.an.paint.presentation

import ColorPicker
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Popup
import com.an.paint.domain.model.toDrawPoint
import com.an.paint.presentation.components.DrawingArea
import com.an.paint.presentation.components.editing.SelectedElementEditPanel
import com.an.paint.presentation.components.TopPanel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaintScreen(
    viewModel: PaintViewModel
) {

    val state = viewModel
        .state
        .collectAsState()
        .value

    var colorPickerExpanded by remember { mutableStateOf(false) }


    Column {
        TopPanel(
            modifier = Modifier,
            selectedShape = state.selectedShape,
            selectedColor = state.selectedColor,
            onSelectShape = { viewModel.onAction(PaintAction.SelectShape(it)) },
            onColorPickerClick = { colorPickerExpanded = it},
            onChangeModeClick = { viewModel.onAction(PaintAction.ChangeMode)},
            isInEditMode = state.isInEditMode
        )
        DrawingArea(
            onTap = {
                viewModel.onAction(PaintAction.TapDrawingArea(it.toDrawPoint()))

            },
            elements = state.elements,
            lastTouchPoint = state.helperPoint,
            onDrag = {
                     viewModel.onAction(PaintAction.DragElement(it))
            },
            dragPaintElement = state.dragPaintElement,
            modifier = Modifier.weight(1f)
        )
        AnimatedVisibility(state.isInEditMode && state.selectedElement != null) {
            if(state.selectedElement != null) {
                SelectedElementEditPanel(
                    element = state.selectedElement,
                    onSubmit = {

                    },
                    onChangeDetails = {
                        viewModel.onAction(PaintAction.EditElement(it))
                    }
                )
            }

        }
    }
    if(colorPickerExpanded) {
        Popup(
            onDismissRequest = {colorPickerExpanded = false}
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ColorPicker(
                    onColorSelected = {
                        viewModel.onAction(PaintAction.PickColor(it))
                        colorPickerExpanded = false
                    },
                    initialColor = state.selectedColor
                )
            }
        }
    }



}