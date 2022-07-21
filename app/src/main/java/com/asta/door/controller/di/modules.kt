package com.asta.door.controller.di

import com.asta.door.controller.data.repository.AuthRepository
import com.asta.door.controller.data.repository.DatabaseRepository
import com.asta.door.controller.ui.home.HomeViewModel
import com.asta.door.controller.ui.login.LoginViewModel
import com.asta.door.controller.ui.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Repositories
    single { AuthRepository() }
    single { DatabaseRepository() }

    // ViewModels
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get(), myUserID = get()) }
}