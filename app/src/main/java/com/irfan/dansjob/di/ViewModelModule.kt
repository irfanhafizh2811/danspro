package com.irfan.dansjob.di

import com.irfan.dansjob.vm.JobDetailViewModel
import com.irfan.dansjob.vm.JobViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { JobViewModel(get()) }
    viewModel { JobDetailViewModel(get()) }
}