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
import com.an.paint.presentation.paint.PaintAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.pow
import kotlin.math.sqrt

class PaintViewModel(
    private val imageProcessor: ImageProcessor
): ViewModel() {

    private val _state = MutableStateFlow(PaintState())
    val state = _state.asStateFlow()

    fun onAction(action: PaintAction) {
        when(action) {
            is PaintAction.TapDrawingArea -> {
                if(state.value.isInEditMode)
                    selectElement(action.p1)
                else
                    addElement(action.p1)

            }
            is PaintAction.SelectShape -> {
                _state.update {
                    it.copy(
                        selectedShape = action.index,
                        helperPoint = null,
                        isInEditMode = false
                    )
                }
            }

            is PaintAction.PickColor -> {
                _state.update { it.copy(
                    selectedColor = action.color,
                    helperPoint = null
                ) }
            }

            PaintAction.ChangeMode -> {
                _state.update { it.copy(
                    isInEditMode = !state.value.isInEditMode,
                    helperPoint = null,
                    selectedElement = null,
                    selectedElementIndex = null
                )}

            }

            is PaintAction.EditElement -> {
                updateList(action.newElement)
            }

            is PaintAction.AddImage -> {
                _state.update { it.copy(
                    elements = state.value.elements + Image(
                        p1 = Offset(x = 0f, y = 0f),
                        bottomRight = action.size,
                        bitmap = action.bitmap,
                        originalBitmap = action.bitmap
                    )
                )}
            }

            is PaintAction.ApplyFilter -> {
                applyFilter(action.filter)
            }

            is PaintAction.TransformElement -> {

                if(state.value.selectedElement == null) return

                val newElement = state.value.selectedElement!!.transform(action.zoom, action.rotation, action.offset)

                updateList(newElement)
            }

            PaintAction.SaveChanges -> {
                _state.update { it.copy(
                    selectedElement = null,
                    selectedElementIndex = null,
                    isInEditMode = false
                ) }
            }
        }
    }

    private fun applyFilter(filter: FilterType) {
        if(state.value.selectedElement is Image) {
            viewModelScope.launch {

                val oldBitmap = (state.value.selectedElement as Image).originalBitmap

                val newBitmap = withContext(Dispatchers.IO) {
                    when(filter) {
                        FilterType.Dilate -> imageProcessor.dilate(oldBitmap)
                        FilterType.Erode -> imageProcessor.erode(oldBitmap)
                        FilterType.Median -> imageProcessor.medianFilter(oldBitmap)
                        FilterType.Smooth -> imageProcessor.smoothingFilter(oldBitmap)
                        FilterType.Sobel -> imageProcessor.sobelFilter(oldBitmap)
                    }
                }



                val newImage = Image(
                    p1 = (state.value.selectedElement as Image).p1,
                    bottomRight = (state.value.selectedElement as Image).bottomRight,
                    bitmap = newBitmap,
                    originalBitmap = (state.value.selectedElement as Image).originalBitmap
                )
                updateList(newImage)
            }
        }
    }

    private fun addElement(p1: Offset) {
        when(state.value.selectedShape) {
            1 -> { // Line
                if(state.value.helperPoint == null) {
                    _state.update { it.copy(
                        helperPoint = p1
                    )}
                } else {
                    val helperPoint = state.value.helperPoint!!
                    _state.update { it.copy(
                        elements = state.value.elements + Line(
                            p1 = helperPoint,
                            end = p1,
                            color = state.value.selectedColor
                        ),
                        helperPoint = null
                    )}
                }
            }
            2 -> { // Circle
                if(state.value.helperPoint == null) {
                    _state.update { it.copy(
                        helperPoint = p1
                    )}
                } else {
                    val helperPoint = state.value.helperPoint!!
                    _state.update { it.copy(
                        elements = state.value.elements + Circle(
                            p1 = helperPoint,
                            radius = calculateRadius(helperPoint, p1),
                            color = state.value.selectedColor
                        ),
                        helperPoint = null
                    )}
                }
            }
            3 -> { // Rectangle
                if(state.value.helperPoint == null) {
                    _state.update { it.copy(
                        helperPoint = p1
                    )}
                } else {
                    val helperPoint = state.value.helperPoint!!
                    _state.update { it.copy(
                        elements = state.value.elements + Rectangle(
                            p1 = helperPoint,
                            bottomRight = p1,
                            color = state.value.selectedColor
                        ),
                        helperPoint = null
                    )}
                }
            }
        }

    }

    private fun selectElement(p1: Offset){
        _state.value.elements.forEachIndexed { index, element  ->
            if (element.containsTouchPoint(p1)){
                _state.update { it.copy(
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
        val newList = state.value.elements.toMutableList().apply {
            this[state.value.selectedElementIndex!!] = newElement
        }.toList()

        _state.update { it.copy(
            elements = newList,
            selectedElement = newElement
        ) }
    }


}