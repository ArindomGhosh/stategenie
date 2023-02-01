package com.arindom.stategenie.di

import com.arindom.stategenie.UsersListRepository
import com.arindom.stategenie.presentation.UserViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { UsersListRepository(androidContext()) }
    viewModel { UserViewModel(get()) }
}