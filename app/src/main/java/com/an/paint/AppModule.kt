package com.an.paint

import com.an.paint.presentation.PaintViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModelOf

val appModule = module {
    viewModel { PaintViewModel() }
}