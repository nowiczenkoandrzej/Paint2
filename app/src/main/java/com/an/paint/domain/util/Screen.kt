package com.an.paint.domain.util

sealed class Screen(val route: String){
    object DrawingArea: Screen(route = "drawing_area")
    object CroppingImage: Screen(route = "crop_image")
}
