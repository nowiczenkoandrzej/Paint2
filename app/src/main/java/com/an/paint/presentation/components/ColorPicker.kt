import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun ColorPicker(
    initialColor: Color = Color.Red,
    onColorSelected: (Color) -> Unit
) {
    var selectedColor by remember { mutableStateOf(initialColor) }

    // RGB values
    var red by remember { mutableStateOf(initialColor.red * 255) }
    var green by remember { mutableStateOf(initialColor.green * 255) }
    var blue by remember { mutableStateOf(initialColor.blue * 255) }

    // CMYK values
    var cyan by remember { mutableStateOf(0f) }
    var magenta by remember { mutableStateOf(0f) }
    var yellow by remember { mutableStateOf(0f) }
    var black by remember { mutableStateOf(0f) }

    // Predefined color palette
    val colorPalette = listOf(
        Color.Red, Color.Green, Color.Blue,
        Color.Yellow, Color.Cyan, Color.Magenta,
        Color.White, Color.Black, Color.Gray,
        Color(0xFFFFA500), Color(0xFFFF69B4), Color(0xFF006400)
    )

    Column(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {

        Row(
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .width(40.dp)
                    .height(136.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(2.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(8.dp))
                    .background(selectedColor)

            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .height(144.dp)
                    .padding(start = 8.dp)

            ) {
                items(colorPalette) { color ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(
                                width = if (selectedColor == color) 2.dp else 1.dp,
                                color = if (selectedColor == color)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface,
                                shape = CircleShape
                            )
                            .background(color)
                            .clickable {
                                selectedColor = color

                                // Update RGB values
                                red = color.red * 255
                                green = color.green * 255
                                blue = color.blue * 255

                                // Update CMYK values
                                val cmyk = rgbToCmyk(color.red, color.green, color.blue)
                                cyan = cmyk.first
                                magenta = cmyk.second
                                yellow = cmyk.third
                                black = cmyk.fourth
                            }
                    )
                }
            }
        }

       Spacer(modifier = Modifier.height(8.dp))

        // RGB Input
        Text(
            text = "RGB:",
            modifier = Modifier.padding(4.dp)
        )

        Column(modifier = Modifier.padding(4.dp)) {
            // Red slider
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "R: ${red.roundToInt()}",
                    modifier = Modifier.width(64.dp)
                )
                Slider(
                    value = red,
                    onValueChange = { newRed ->
                        red = newRed
                        // Update color and CMYK values
                        val newColor = Color(red / 255f, green / 255f, blue / 255f)
                        selectedColor = newColor

                        // Update CMYK values
                        val cmyk = rgbToCmyk(newColor.red, newColor.green, newColor.blue)
                        cyan = cmyk.first
                        magenta = cmyk.second
                        yellow = cmyk.third
                        black = cmyk.fourth
                    },
                    valueRange = 0f..255f
                )

            }

            // Green slider
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "G: ${green.roundToInt()}",
                    modifier = Modifier.width(64.dp)
                )
                Slider(
                    value = green,
                    onValueChange = { newGreen ->
                        green = newGreen
                        // Update color and CMYK values
                        val newColor = Color(red / 255f, green / 255f, blue / 255f)
                        selectedColor = newColor

                        // Update CMYK values
                        val cmyk = rgbToCmyk(newColor.red, newColor.green, newColor.blue)
                        cyan = cmyk.first
                        magenta = cmyk.second
                        yellow = cmyk.third
                        black = cmyk.fourth
                    },
                    valueRange = 0f..255f
                )

            }

            // Blue slider
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "B: ${blue.roundToInt()}",
                    modifier = Modifier.width(64.dp)
                )
                Slider(
                    value = blue,
                    onValueChange = { newBlue ->
                        blue = newBlue
                        // Update color and CMYK values
                        val newColor = Color(red / 255f, green / 255f, blue / 255f)
                        selectedColor = newColor

                        // Update CMYK values
                        val cmyk = rgbToCmyk(newColor.red, newColor.green, newColor.blue)
                        cyan = cmyk.first
                        magenta = cmyk.second
                        yellow = cmyk.third
                        black = cmyk.fourth
                    },
                    valueRange = 0f..255f
                )

            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // CMYK Input
        Text(
            text = "CMYK:",
            modifier = Modifier.padding(4.dp)
        )


        Column(
            modifier = Modifier
                .padding(4.dp)
                .scrollable(
                    rememberScrollState(),
                    Orientation.Vertical
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "C: ${(cyan * 100).roundToInt()}%",
                    modifier = Modifier.width(64.dp)
                )
                Slider(
                    value = cyan,
                    onValueChange = { newCyan ->
                        cyan = newCyan
                        // Update color and RGB values
                        val rgb = cmykToRgb(cyan, magenta, yellow, black)
                        red = rgb.first * 255
                        green = rgb.second * 255
                        blue = rgb.third * 255

                        val newColor = Color(rgb.first, rgb.second, rgb.third)
                        selectedColor = newColor
                    },
                    valueRange = 0f..1f
                )

            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "M: ${(magenta * 100).roundToInt()}%",
                    modifier = Modifier.width(64.dp)
                )
                Slider(
                    value = magenta,
                    onValueChange = { newMagenta ->
                        magenta = newMagenta
                        // Update color and RGB values
                        val rgb = cmykToRgb(cyan, magenta, yellow, black)
                        red = rgb.first * 255
                        green = rgb.second * 255
                        blue = rgb.third * 255

                        val newColor = Color(rgb.first, rgb.second, rgb.third)
                        selectedColor = newColor
                    },
                    valueRange = 0f..1f
                )

            }

            // Yellow slider
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text ="Y: ${(yellow * 100).roundToInt()}%",
                    modifier = Modifier.width(64.dp)
                )
                Slider(
                    value = yellow,
                    onValueChange = { newYellow ->
                        yellow = newYellow
                        // Update color and RGB values
                        val rgb = cmykToRgb(cyan, magenta, yellow, black)
                        red = rgb.first * 255
                        green = rgb.second * 255
                        blue = rgb.third * 255

                        val newColor = Color(rgb.first, rgb.second, rgb.third)
                        selectedColor = newColor
                    },
                    valueRange = 0f..1f
                )

            }

            // Black slider
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text ="K: ${(black * 100).roundToInt()}%",
                    modifier = Modifier.width(64.dp)
                )
                Slider(
                    value = black,
                    onValueChange = { newBlack ->
                        black = newBlack
                        // Update color and RGB values
                        val rgb = cmykToRgb(cyan, magenta, yellow, black)
                        red = rgb.first * 255
                        green = rgb.second * 255
                        blue = rgb.third * 255

                        val newColor = Color(rgb.first, rgb.second, rgb.third)
                        selectedColor = newColor
                    },
                    valueRange = 0f..1f
                )
            }
            Button(
                onClick = {
                    onColorSelected(selectedColor)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Select Color")
            }
        }
    }
}



private fun cmykToRgb(c: Float, m: Float, y: Float, k: Float): Triple<Float, Float, Float> {
    val r = (1 - c) * (1 - k)
    val g = (1 - m) * (1 - k)
    val b = (1 - y) * (1 - k)

    return Triple(r, g, b)
}


private fun rgbToCmyk(r: Float, g: Float, b: Float): Quadruple<Float, Float, Float, Float> {
    if (r == 0f && g == 0f && b == 0f) {
        return Quadruple(0f, 0f, 0f, 1f)
    }

    val k = 1 - maxOf(r, g, b)
    val c = (1 - r - k) / (1 - k)
    val m = (1 - g - k) / (1 - k)
    val y = (1 - b - k) / (1 - k)

    return Quadruple(c, m, y, k)
}

private data class Quadruple<A, B, C, D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)