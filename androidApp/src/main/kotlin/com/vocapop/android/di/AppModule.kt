package com.vocapop.android.di

import com.vocapop.android.ui.learning.SessionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { SessionViewModel(get()) }
}
