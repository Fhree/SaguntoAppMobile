package com.sagunto.saguntoappmobile

import com.sagunto.saguntoappmobile.data.network.provideHttpClient
import com.sagunto.saguntoappmobile.data.repository.OrderRepository
import com.sagunto.saguntoappmobile.data.repository.ProductRepository
import com.sagunto.saguntoappmobile.data.repository.UserRepository
import com.sagunto.saguntoappmobile.data.interfaces.IOrderRepository
import com.sagunto.saguntoappmobile.data.interfaces.IProductRepository
import com.sagunto.saguntoappmobile.data.interfaces.IUserRepository
import com.sagunto.saguntoappmobile.data.repository.AuthRepository
import com.sagunto.saguntoappmobile.ui.viewmodels.AddOrderViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.AddProductViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.AddUserViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.LoginViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.UnpaidOrderViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.SelectCustomerTypeViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val module = module {
    // --- CAPA DE RED ---
    single { provideHttpClient() }

    // --- CAPA DE DATOS (Repositorios, Bases de datos, APIs) ---
    singleOf(::AuthRepository)
    singleOf(::ProductRepository) { bind<IProductRepository>() }
    singleOf(::UserRepository) { bind<IUserRepository>() }
    singleOf(::OrderRepository) { bind<IOrderRepository>() }

    // --- CAPA DE PRESENTACIÓN (ViewModels) ---
    viewModelOf(::LoginViewModel)
    viewModelOf(::AddProductViewModel)
    viewModelOf(::AddUserViewModel)
    viewModelOf(::AddOrderViewModel)
    viewModelOf(::SelectCustomerTypeViewModel)
    viewModelOf(::UnpaidOrderViewModel)
    //viewModelOf(::CheckConsumptionViewModel)
}