package com.an.paint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.an.paint.presentation.PaintScreen
import com.an.paint.presentation.PaintViewModel
import com.an.paint.ui.theme.PaintTheme
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaintTheme {

                val viewModel = koinViewModel<PaintViewModel>()

                PaintScreen(
                    viewModel = viewModel
                )

            }
        }
    }
}




