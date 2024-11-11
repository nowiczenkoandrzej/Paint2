package com.an.paint.presentation.paint

import ColorPicker
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Popup
import androidx.navigation.NavController
import com.an.paint.data.toBitmap
import com.an.paint.domain.util.Screen
import com.an.paint.presentation.PaintViewModel
import com.an.paint.presentation.components.DrawingArea
import com.an.paint.presentation.components.editing.SelectedElementEditPanel
import com.an.paint.presentation.components.TopPanel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaintScreen(
    viewModel: PaintViewModel,
    navController: NavController
) {

    val state = viewModel
        .paintState
        .collectAsState()
        .value

    var colorPickerExpanded by remember { mutableStateOf(false) }


    val context = LocalContext.current


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri: Uri? ->

        uri?.let {

            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }

            context.contentResolver.openInputStream(uri).use { input ->
                BitmapFactory.decodeStream(input, null, options)
            }
            val width = options.outWidth.toFloat()
            val height = options.outHeight.toFloat()

            viewModel.onAction(
                PaintAction.AddImage(
                bitmap = uri.toBitmap(context),
                size = Offset(width, height)
            ))
        }

    }

    Column {
        TopPanel(
            modifier = Modifier,
            selectedShape = state.selectedShape,
            selectedColor = state.selectedColor,
            onSelectShape = { viewModel.onAction(PaintAction.SelectShape(it)) },
            onColorPickerClick = { colorPickerExpanded = it},
            onChangeModeClick = { viewModel.onAction(PaintAction.ChangeMode)},
            isInEditMode = state.isInEditMode,
            onPickImage = { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
        )

        DrawingArea(
            onTap = {
                viewModel.onAction(PaintAction.TapDrawingArea(it))

            },
            elements = state.elements,
            lastTouchPoint = state.helperPoint,

            onTransform = { zoom, rotation, offset ->
                viewModel.onAction(PaintAction.TransformElement(zoom, rotation, offset))
            },
            selectedElementIndex = state.selectedElementIndex,
            selectedElement = state.selectedElement
        )
        Spacer(modifier = Modifier.weight(1f))
        AnimatedVisibility(state.isInEditMode && state.selectedElement != null) {
            if(state.selectedElement != null) {
                SelectedElementEditPanel(
                    element = state.selectedElement,
                    onSubmit = {
                        viewModel.onAction(PaintAction.SaveChanges)
                    },
                    onChangeDetails = {
                        viewModel.onAction(PaintAction.EditElement(it))
                    },
                    onApplyFilter = {
                        viewModel.onAction(PaintAction.ApplyFilter(it))
                    },
                    onCutImage = {
                        viewModel.onAction(PaintAction.Navigate(Screen.CroppingImage.route))
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