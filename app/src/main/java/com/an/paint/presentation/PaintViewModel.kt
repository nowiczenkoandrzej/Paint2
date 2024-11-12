package com.an.paint.presentation


import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.paint.domain.FilterType
import com.an.paint.domain.model.Circle
import com.an.paint.domain.model.Image
import com.an.paint.domain.model.Line
import com.an.paint.domain.model.Rectangle
import com.an.paint.domain.ImageProcessor
import com.an.paint.domain.util.Element
import com.an.paint.domain.util.Screen
import com.an.paint.domain.util.Shape
import com.an.paint.presentation.crop.CropState
import com.an.paint.presentation.paint.PaintAction
import com.an.paint.presentation.paint.PaintState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.pow
import kotlin.math.sqrt

class PaintViewModel(
    private val imageProcessor: ImageProcessor
): ViewModel() {

    private val _paintState = MutableStateFlow(PaintState())
    val paintState = _paintState.asStateFlow()

    private val _cropState = MutableStateFlow(CropState())
    val cropState = _cropState.asStateFlow()

    private val _events = Channel<PaintEvent>()
    val events = _events.receiveAsFlow()
    fun onAction(action: PaintAction) {
        when(action) {
            is PaintAction.EditElement -> updateList(action.newElement)
            is PaintAction.ApplyFilter -> applyFilter(action.filter)

            is PaintAction.TapDrawingArea -> {
                if(paintState.value.isInEditMode)
                    selectElement(action.p1)
                else
                    addElement(action.p1)
            }

            is PaintAction.SelectShape -> {
                _paintState.update {
                    it.copy(
                        selectedShape = action.shape,
                        helperPoint = null,
                        isInEditMode = false
                    )
                }
            }
            is PaintAction.PickColor -> {
                _paintState.update { it.copy(
                    selectedColor = action.color,
                    helperPoint = null
                ) }
            }

            PaintAction.ChangeMode -> {
                _paintState.update { it.copy(
                    isInEditMode = !paintState.value.isInEditMode,
                    helperPoint = null,
                    selectedElement = null,
                    selectedElementIndex = null
                )}
            }

            is PaintAction.AddImage -> {
                _paintState.update { it.copy(
                    elements = paintState.value.elements + Image(
                        p1 = Offset(x = 0f, y = 0f),
                        bottomRight = action.size,
                        bitmap = action.bitmap,
                        originalBitmap = action.bitmap
                    )
                )}
            }

            is PaintAction.TransformElement -> {
                if(paintState.value.selectedElement == null) return
                val newElement = paintState.value.selectedElement!!.transform(action.zoom, action.rotation, action.offset)
                updateList(newElement)
            }

            PaintAction.SaveChanges -> {
                _paintState.update { it.copy(
                    selectedElement = null,
                    selectedElementIndex = null,
                    isInEditMode = false
                ) }
            }

            is PaintAction.CutImage -> {

            }

            is PaintAction.SetCuttingFrame -> {
                _cropState.update { it.copy(
                    topLeft = action.topLeft,
                    bottomRight = action.bottomRight
                ) }
            }

            is PaintAction.PickImageToCut -> {
                viewModelScope.launch {
                    _events.send(PaintEvent.Navigate(Screen.CroppingImage.route))

                }
            }
        }
    }

    private fun applyFilter(filter: FilterType) {
        if(paintState.value.selectedElement is Image) {
            viewModelScope.launch {

                val element = paintState.value.selectedElement as Image

                val newBitmap = withContext(Dispatchers.IO) {
                    when(filter) {
                        FilterType.Dilate -> imageProcessor.dilate(element.originalBitmap)
                        FilterType.Erode -> imageProcessor.erode(element.originalBitmap)
                        FilterType.Median -> imageProcessor.medianFilter(element.originalBitmap)
                        FilterType.Smooth -> imageProcessor.smoothingFilter(element.originalBitmap)
                        FilterType.Sobel -> imageProcessor.sobelFilter(element.originalBitmap)
                    }
                }

                val newImage = Image(
                    p1 = element.p1,
                    bottomRight = element.bottomRight,
                    bitmap = newBitmap,
                    originalBitmap = element.originalBitmap,
                    rotationAngle = element.rotationAngle,
                    zoom = element.zoom
                )
                updateList(newImage)
            }
        }
    }

    private fun addElement(p1: Offset) {
        when(paintState.value.selectedShape) {
            Shape.LINE -> {
                if(paintState.value.helperPoint == null) {
                    _paintState.update { it.copy(
                        helperPoint = p1
                    )}
                } else {
                    val helperPoint = paintState.value.helperPoint!!
                    _paintState.update { it.copy(
                        elements = paintState.value.elements + Line(
                            p1 = helperPoint,
                            end = p1,
                            color = paintState.value.selectedColor
                        ),
                        helperPoint = null
                    )}
                }
            }
            Shape.CIRCLE -> {
                if(paintState.value.helperPoint == null) {
                    _paintState.update { it.copy(
                        helperPoint = p1
                    )}
                } else {
                    val helperPoint = paintState.value.helperPoint!!
                    _paintState.update { it.copy(
                        elements = paintState.value.elements + Circle(
                            p1 = helperPoint,
                            radius = calculateRadius(helperPoint, p1),
                            color = paintState.value.selectedColor
                        ),
                        helperPoint = null
                    )}
                }
            }
            Shape.RECTANGLE -> {
                if(paintState.value.helperPoint == null) {
                    _paintState.update { it.copy(
                        helperPoint = p1
                    )}
                } else {
                    val helperPoint = paintState.value.helperPoint!!
                    _paintState.update { it.copy(
                        elements = paintState.value.elements + Rectangle(
                            p1 = helperPoint,
                            bottomRight = p1,
                            color = paintState.value.selectedColor
                        ),
                        helperPoint = null
                    )}
                }
            }
        }

    }

    private fun selectElement(p1: Offset){
        _paintState.value.elements.forEachIndexed { index, element  ->
            if (element.containsTouchPoint(p1)){
                _paintState.update { it.copy(
                    selectedElement = element,
                    helperPoint = null,
                    selectedElementIndex = index
                )}
            }
        }
    }

    private fun calculateRadius(p1: Offset, p2: Offset): Float {
        return sqrt((p2.x - p1.x).pow(2) + (p2.y - p1.y).pow(2))
    }

    private fun updateList(newElement: Element){
        val newList = paintState.value.elements.toMutableList().apply {
            this[paintState.value.selectedElementIndex!!] = newElement
        }.toList()

        _paintState.update { it.copy(
            elements = newList,
            selectedElement = newElement
        ) }
    }


}