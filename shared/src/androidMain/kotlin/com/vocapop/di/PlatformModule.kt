package com.vocapop.di

import com.vocapop.data.local.AndroidDatabaseDriverFactory
import com.vocapop.data.local.DatabaseDriverFactory
import org.koin.dsl.module

actual val platformModule = module {
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(get()) }
}
