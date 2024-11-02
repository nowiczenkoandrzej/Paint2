package com.an.paint.presentation.components.editing

import ColorPicker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.an.paint.domain.model.Circle
import com.an.paint.domain.model.DrawPoint
import com.an.paint.domain.model.Line

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CircleEditPanel(
    onChangeDetails: (Circle) -> Unit,
    modifier: Modifier = Modifier,
    editedCircle: Circle
) {


    var colorPickerExpanded by remember { mutableStateOf(false) }


    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            Text(text = "Center:")
            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = String.format("%.0f", editedCircle.center.x),
                onValueChange = {
                    val newCircle = Circle(
                        center = DrawPoint(
                            x = it.toFloat(),
                            y = editedCircle.center.y
                        ),
                        radius = editedCircle.radius,
                        color = editedCircle.color
                    )
                    onChangeDetails(newCircle)
                },
                label = { Text(text = "Y") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = String.format("%.0f", editedCircle.center.y),
                onValueChange = {
                    val newCircle = Circle(
                        center = DrawPoint(
                            x = editedCircle.center.x,
                            y = it.toFloat()
                        ),
                        radius = editedCircle.radius,
                        color = editedCircle.color
                    )
                    onChangeDetails(newCircle)
                },
                label = { Text(text = "Y") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            Text(text = "Radius:")
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = String.format("%.0f", editedCircle.radius),
                onValueChange = {
                    val newCircle = Circle(
                        center = DrawPoint(
                            x = editedCircle.center.x,
                            y = editedCircle.center.y
                        ),
                        radius = it.toFloat(),
                        color = editedCircle.color
                    )
                    onChangeDetails(newCircle)
                },
                label = { Text(text = "Radius") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Color ")
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .background(editedCircle.color)
                    .clickable {
                        colorPickerExpanded = true
                    }
            )
        }
    }

    if(colorPickerExpanded) {
        Popup(
            onDismissRequest = {colorPickerExpanded = false}
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ColorPicker(
                    onColorSelected = {
                        val newCircle = Circle(
                            center = editedCircle.center,
                            radius = editedCircle.radius,
                            color = it
                        )
                        colorPickerExpanded = false
                        onChangeDetails(newCircle)
                    },
                    initialColor = editedCircle.color
                )
            }
        }
    }
}