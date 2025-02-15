package com.an.paint.presentation.components

import ColorPicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.an.paint.R
import com.an.paint.domain.util.Shape

@Composable
fun TopPanel(
    modifier: Modifier = Modifier,
    selectedShape: Shape,
    selectedColor: Color = Color.Black,
    onSelectShape: (Shape) -> Unit,
    onColorPickerClick: (Boolean) -> Unit,
    onChangeModeClick: () -> Unit,
    isInEditMode: Boolean = false,
    onPickImage: () -> Unit,
    onSave: () -> Unit,
    onNewDrawing: () -> Unit
) {

    var shapePickerExpanded by remember { mutableStateOf(false) }

    var editButtonBackgroundColor = if(isInEditMode) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.primary
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box {
            IconButton(onClick = { shapePickerExpanded = true }) {
                when(selectedShape) {
                    Shape.LINE -> {
                        Icon(
                            painter = painterResource(id = R.drawable.line_24),
                            contentDescription = Shape.LINE.name,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Shape.CIRCLE -> {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_circle_24),
                            contentDescription = Shape.CIRCLE.name,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    Shape.RECTANGLE -> {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_rectangle_24),
                            contentDescription = Shape.RECTANGLE.name,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Shape.CURVE -> {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_gesture_24),
                            contentDescription = "bezier curve",
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
            DropdownMenu(expanded = shapePickerExpanded, onDismissRequest = { shapePickerExpanded = false }) {
                DropdownMenuItem(
                    text = { Text(text = "Line") },
                    onClick = {
                        shapePickerExpanded = false
                        onSelectShape(Shape.LINE)
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.line_24),
                            contentDescription = "Line"
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = "Circle") },
                    onClick = {
                        shapePickerExpanded = false
                        onSelectShape(Shape.CIRCLE)
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_circle_24),
                            contentDescription = "Circle"
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = "Rectangle") },
                    onClick = {
                        shapePickerExpanded = false
                        onSelectShape(Shape.RECTANGLE)
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_rectangle_24),
                            contentDescription = "Rectangle"
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = "Bezier Curve") },
                    onClick = {
                        shapePickerExpanded = false
                        onSelectShape(Shape.CURVE)
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_gesture_24),
                            contentDescription = "bezier Curve"
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = "Image") },
                    onClick = {
                        shapePickerExpanded = false
                        onPickImage()
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_image_24),
                            contentDescription = "Image"
                        )
                    }
                )

            }
        }

        IconButton(
            onClick = { onChangeModeClick() },
            modifier = Modifier
                .padding(4.dp)
                .clip(RoundedCornerShape(CornerSize(20.dp)))
                .background(editButtonBackgroundColor)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.outline_mode_edit_outline_24),
                contentDescription = "grab",
                modifier = Modifier
                    .size(48.dp),
            )
        }

        IconButton(onClick = {
            onColorPickerClick(true)
        }) {
            Icon(
                painter = painterResource(id = R.drawable.outline_palette_24),
                contentDescription = "pick color",
                modifier = Modifier
                    .size(48.dp),
                tint = selectedColor
            )
        }

        IconButton(onClick = {
            onNewDrawing()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_fiber_new_24),
                contentDescription = "pick color",
                modifier = Modifier
                    .size(48.dp),
                tint = selectedColor
            )
        }

        IconButton(onClick = {
            onSave()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_save_24),
                contentDescription = "pick color",
                modifier = Modifier
                    .size(48.dp),
                tint = selectedColor
            )
        }


    }

}