package com.an.paint

import com.an.paint.data.ImageProcessorImpl
import com.an.paint.domain.ImageProcessor
import com.an.paint.presentation.PaintViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind

val appModule = module {

    singleOf(::ImageProcessorImpl).bind<ImageProcessor>()

    //viewModel { PaintViewModel() }

    viewModelOf(::PaintViewModel)
}