package com.an.paint.presentation

sealed interface PaintEvent {
    data class Error(val error: String): PaintEvent
    data class Navigate(val route: String): PaintEvent
}