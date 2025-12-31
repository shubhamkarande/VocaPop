package com.vocapop.di

import com.vocapop.data.local.DatabaseHelper
import com.vocapop.data.remote.AuthService
import com.vocapop.data.remote.createHttpClient
import com.vocapop.data.repository.CardRepositoryImpl
import com.vocapop.data.repository.UserRepositoryImpl
import com.vocapop.domain.repository.CardRepository
import com.vocapop.domain.repository.UserRepository
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(platformModule, commonModule)
}

val commonModule = module {
    single { DatabaseHelper(get()) }
    single { createHttpClient() }
    
    single { AuthService(get()) }
    
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<CardRepository> { CardRepositoryImpl(get()) }
}
