package com.an.paint

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.an.paint.domain.util.Screen
import com.an.paint.presentation.ObserveAsEvents
import com.an.paint.presentation.PaintEvent
import com.an.paint.presentation.crop.ImageCropperScreen
import com.an.paint.presentation.paint.PaintScreen
import com.an.paint.presentation.PaintViewModel
import com.an.paint.ui.theme.PaintTheme
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaintTheme {

                val navController = rememberNavController()

                val viewModel = koinViewModel<PaintViewModel>()

                val context = LocalContext.current

                ObserveAsEvents(
                    events = viewModel.events
                ) { event ->
                    when(event) {
                        is PaintEvent.Error -> {
                            Toast.makeText(
                                context,
                                event.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is PaintEvent.Navigate -> {
                            navController.navigate(event.route)
                        }
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = Screen.DrawingArea.route
                ) {
                    composable(route = Screen.DrawingArea.route) {
                        PaintScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                    composable(route = Screen.CroppingImage.route) {
                        Log.d("TAG", "ImageCropperScreen nav: ${viewModel.paintState.value.selectedElement}")
                        ImageCropperScreen(
                            viewModel = viewModel,
                            navController = navController
                        )
                    }
                }



            }
        }
    }
}




