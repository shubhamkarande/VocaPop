package com.vocapop.di

import com.vocapop.data.local.DatabaseDriverFactory
import com.vocapop.data.local.IosDatabaseDriverFactory
import org.koin.dsl.module

actual val platformModule = module {
    single<DatabaseDriverFactory> { IosDatabaseDriverFactory() }
}
