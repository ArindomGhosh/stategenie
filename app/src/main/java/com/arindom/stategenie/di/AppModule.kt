package com.arindom.stategenie.di

import com.arindom.stategenie.UsersListRepository
import com.arindom.stategenie.data.UserListService
import com.arindom.stategenie.presentation.UserViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { provideRetrofit() }
    single { provideNetworkApi(get()) }
}

val appModule = module {
    single { UsersListRepository(androidContext(), get()) }
    viewModel { UserViewModel(get()) }
}

fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient
            .Builder()
            .build())
        .build()
}

fun provideNetworkApi(retrofit: Retrofit): UserListService =
    retrofit.create(UserListService::class.java)