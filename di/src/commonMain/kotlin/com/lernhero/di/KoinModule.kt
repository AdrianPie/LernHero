package com.lernhero.di

import com.lernhero.auth.AuthViewModel
import com.lernhero.data.PlayerRepositoryImpl
import com.lernhero.data.domain.PlayerRepository
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    single<PlayerRepository> { PlayerRepositoryImpl() }
    viewModelOf(::AuthViewModel)
}
fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null
) {
    startKoin {
        config?.invoke(this)
    }
}


