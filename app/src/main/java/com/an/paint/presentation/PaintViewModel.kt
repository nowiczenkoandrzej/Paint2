package com.an.paint.presentation

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import com.an.paint.domain.model.Circle
import com.an.paint.domain.model.DrawPoint
import com.an.paint.domain.model.Line
import com.an.paint.domain.model.Rectangle
import com.an.paint.domain.util.Element
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.pow
import kotlin.math.sqrt

class PaintViewModel(

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

            is PaintAction.DragElement -> {
                if(state.value.isInEditMode && state.value.selectedElement != null)
                    dragElement(action.offset)
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

                Log.d("TAG", "PaintScreen viewModel: ${state.value}")
            }

            is PaintAction.EditElement -> {
                val newList = state.value.elements.toMutableList().apply {
                    this[state.value.selectedElementIndex!!] = action.newElement
                }.toList()

                _state.update { it.copy(
                    elements = newList,
                    selectedElement = action.newElement
                ) }
            }
        }
    }

    private fun addElement(p1: DrawPoint) {
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
                            start = helperPoint,
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
                            center = helperPoint,
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
                            topLeft = helperPoint,
                            bottomRight = p1,
                            color = state.value.selectedColor
                        ),
                        helperPoint = null
                    )}
                }

            }
        }

    }

    private fun selectElement(p1: DrawPoint){
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

    private fun calculateRadius(p1: DrawPoint, p2: DrawPoint): Float {
        return sqrt((p2.x - p1.x).pow(2) + (p2.y - p1.y).pow(2))
    }

    private fun dragElement(offset: Offset) {

        var newElement = when(state.value.selectedElement) {
            is Line -> {
                Line(
                    start = DrawPoint(
                        x = (state.value.selectedElement as Line).start.x + offset.x,
                        y = (state.value.selectedElement as Line).start.y + offset.y
                    ),
                    end = DrawPoint(
                        x = (state.value.selectedElement as Line).end.x + offset.x,
                        y = (state.value.selectedElement as Line).end.y + offset.y
                    ),
                    color = (state.value.selectedElement as Line).color
                )
            }
            is Circle -> {
                Circle(
                    center = DrawPoint(
                        x = (state.value.selectedElement as Circle).center.x + offset.x,
                        y = (state.value.selectedElement as Circle).center.y + offset.y
                    ),
                    radius = (state.value.selectedElement as Circle).radius,
                    color = (state.value.selectedElement as Circle).color
                )
            }
            is Rectangle -> {
                Rectangle(
                    topLeft = DrawPoint(
                        x = (state.value.selectedElement as Rectangle).topLeft.x + offset.x,
                        y = (state.value.selectedElement as Rectangle).topLeft.y + offset.y
                    ),
                    bottomRight = DrawPoint(
                        x = (state.value.selectedElement as Rectangle).bottomRight.x + offset.x,
                        y = (state.value.selectedElement as Rectangle).bottomRight.y + offset.y
                    ),
                    color = (state.value.selectedElement as Rectangle).color
                )
            }

            else -> {
                state.value.selectedElement
            }
        }

        val newList = state.value.elements.toMutableList().apply {
            this[state.value.selectedElementIndex!!] = newElement!!
        }.toList()

        _state.update { it.copy(
            elements = newList,
            selectedElement = newElement
        ) }
    }


}