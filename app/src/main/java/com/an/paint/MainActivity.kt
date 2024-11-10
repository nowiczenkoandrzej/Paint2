package com.an.paint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.an.paint.domain.util.Screen
import com.an.paint.presentation.ImageCropperScreen
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




