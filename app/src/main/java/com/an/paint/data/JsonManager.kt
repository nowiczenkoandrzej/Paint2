package com.an.paint.data

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.paint.domain.model.BezierCurve
import com.an.paint.domain.model.Circle
import com.an.paint.domain.model.ColorAdapter
import com.an.paint.domain.model.Element
import com.an.paint.domain.model.Image
import com.an.paint.domain.model.Line
import com.an.paint.domain.model.OffsetAdapter
import com.an.paint.domain.model.Rectangle
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type


class JsonManager(
    private val context: Context
) {

    private val gson = GsonBuilder()
        .registerTypeAdapter(Color::class.java, ColorAdapter())
        .registerTypeAdapter(Offset::class.java, OffsetAdapter())
        .registerTypeAdapter(Element::class.java, ElementAdapter())
        .create()

    fun saveElementsToFile(elements: List<Element>) {
        val file = File(context.filesDir,"elements.json")
        val json = gson.toJson(elements) // serializacja listy elementów do JSON
        file.writeText(json)
    }

    fun loadElementsFromFile(): List<Element> {
        val json = File(context.filesDir, "elements.json").readText() // wczytanie danych z pliku
        return gson.fromJson(json, object : TypeToken<List<Element>>() {}.type) // deserializacja
    }

}

class ElementAdapter : JsonSerializer<Element>, JsonDeserializer<Element> {

    override fun serialize(src: Element?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val jsonObject = JsonObject()

        // Dodajemy pole 'type', które będzie przechowywać typ klasy
        when (src) {
            is Rectangle -> {
                jsonObject.addProperty("type", "Rectangle") // Typ klasy
                jsonObject.add("bottomRight", Gson().toJsonTree(src.bottomRight)) // Inne pola
                jsonObject.addProperty("color", src.color.toString())
            }
            is Circle -> {
                jsonObject.addProperty("type", "Circle") // Typ klasy
                jsonObject.addProperty("radius", src.radius) // Inne pola
                jsonObject.addProperty("color", src.color.toString())
            }
            else -> throw JsonParseException("Unknown element type")
        }
        return jsonObject
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Element {
        val jsonObject = json?.asJsonObject
        val type = jsonObject?.get("type")?.asString // Pobieramy typ z JSON

        return when (type) {
            "Rectangle" -> {
                val bottomRight = Gson().fromJson(jsonObject?.get("bottomRight"), Offset::class.java)
                Rectangle(
                    color = Color.Black, // Wypełnij odpowiednimi wartościami
                    rotationAngle = 0f,
                    p1 = Offset.Zero,
                    zoom = 1f,
                    bottomRight = bottomRight
                )
            }
            "Circle" -> {
                val radius = jsonObject?.get("radius")?.asFloat ?: 0f
                Circle(
                    color = Color.Black, // Wypełnij odpowiednimi wartościami
                    rotationAngle = 0f,
                    p1 = Offset.Zero,
                    zoom = 1f,
                    radius = radius
                )
            }
            else -> throw JsonParseException("Unknown element type: $type")
        }
    }
}
