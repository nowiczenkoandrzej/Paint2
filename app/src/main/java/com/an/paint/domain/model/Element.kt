package com.an.paint.domain.model


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

sealed interface Element{

    val color: Color
    val rotationAngle: Float
    val p1: Offset?
    val zoom: Float

    fun containsTouchPoint(point: Offset): Boolean
    fun changeColor(color: Color): Element
    fun transform(zoom: Float, rotation: Float, offset: Offset, centroid: Offset): Element
}
class OffsetAdapter : JsonSerializer<Offset>, JsonDeserializer<Offset> {
    override fun serialize(src: Offset, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonObject().apply {
            addProperty("x", src.x)
            addProperty("y", src.y)
        }
    }

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Offset {
        val jsonObject = json.asJsonObject
        val x = jsonObject["x"].asFloat
        val y = jsonObject["y"].asFloat
        return Offset(x, y)
    }
}

class ColorAdapter : JsonSerializer<Color>, JsonDeserializer<Color> {
    override fun serialize(src: Color, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonObject().apply {
            addProperty("red", src.red)
            addProperty("green", src.green)
            addProperty("blue", src.blue)
            addProperty("alpha", src.alpha)
        }
    }

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Color {
        val jsonObject = json.asJsonObject
        val red = jsonObject["red"].asFloat
        val green = jsonObject["green"].asFloat
        val blue = jsonObject["blue"].asFloat
        val alpha = jsonObject["alpha"].asFloat
        return Color(red, green, blue, alpha)
    }
}

