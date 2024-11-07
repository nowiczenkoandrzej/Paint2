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
import com.an.paint.domain.model.DrawPoint

import com.an.paint.domain.model.Line

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LineEditPanel(
    onChangeDetails: (Line) -> Unit,
    modifier: Modifier = Modifier,
    editedLine: Line
) {


    var colorPickerExpanded by remember { mutableStateOf(false) }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            Text(text = "Start:")
            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = String.format("%.0f", editedLine.p1.x),
                onValueChange = {
                    var newValue = it
                    if(newValue.isEmpty()) newValue = "0"

                    val newLine = Line(
                        p1 = DrawPoint(
                            x = newValue.toFloat(),
                            y = editedLine.p1.y
                        ),
                        end = editedLine.end,
                        color = editedLine.color
                    )
                    onChangeDetails(newLine)
                },
                label = { Text(text = "X") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = String.format("%.0f", editedLine.p1.y),
                onValueChange = {
                    var newValue = it
                    if(newValue.isEmpty()) newValue = "0"

                    val newLine = Line(
                        p1 = DrawPoint(
                            x = editedLine.p1.x,
                            y = newValue.toFloat()
                        ),
                        end = editedLine.end,
                        color = editedLine.color
                    )
                    onChangeDetails(newLine)
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
            Text(text = "End:")
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = String.format("%.0f", editedLine.end.x),
                onValueChange = {
                    var newValue = it
                    if(newValue.isEmpty()) newValue = "0"

                    val newLine = Line(
                        p1 = editedLine.p1,
                        end = DrawPoint(
                            x = newValue.toFloat(),
                            y = editedLine.end.y
                        ),
                        color = editedLine.color
                    )
                    onChangeDetails(newLine)
                },
                label = { Text(text = "X") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = String.format("%.0f", editedLine.end.y),
                onValueChange = {
                    var newValue = it
                    if(newValue.isEmpty()) newValue = "0"

                    val newLine = Line(
                        p1 = editedLine.p1,
                        end = DrawPoint(
                            x = editedLine.end.x,
                            y = newValue.toFloat()
                        ),
                        color = editedLine.color
                    )
                    onChangeDetails(newLine)
                },
                label = { Text(text = "Y") },
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
                    .background(editedLine.color)
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
                        val newLine = Line(
                            p1 = editedLine.p1,
                            end = editedLine.end,
                            color = it
                        )
                        colorPickerExpanded = false
                        onChangeDetails(newLine)
                    },
                    initialColor = editedLine.color
                )
            }
        }
    }



}