package com.sagunto.saguntoappmobile

import com.sagunto.saguntoappmobile.data.network.provideHttpClient
import com.sagunto.saguntoappmobile.data.repository.ProductRepository
import com.sagunto.saguntoappmobile.data.repository.UserRepository
import com.sagunto.saguntoappmobile.domain.interfaces.IProductRepository
import com.sagunto.saguntoappmobile.domain.interfaces.IUserRepository
import com.sagunto.saguntoappmobile.ui.viewmodels.AddProductViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.AddUserViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val module = module {
    // --- CAPA DE RED ---
    single { provideHttpClient() }

    // --- CAPA DE DATOS (Repositorios, Bases de datos, APIs) ---
    singleOf(::ProductRepository) { bind<IProductRepository>() }
    singleOf(::UserRepository) { bind<IUserRepository>() }
    // single<OrderRepository> { OrderRepositoryImpl() }

    // --- CAPA DE PRESENTACIÓN (ViewModels) ---
    viewModelOf(::AddProductViewModel)
    viewModelOf(::AddUserViewModel)
    // viewModelOf(::AddOrderViewModel)
    // viewModelOf(::CheckConsumptionViewModel)
}