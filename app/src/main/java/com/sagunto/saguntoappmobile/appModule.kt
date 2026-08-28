package com.sagunto.saguntoappmobile

import androidx.room.Room
import androidx.work.WorkManager
import com.sagunto.saguntoappmobile.data.local.SaguntoDatabase
import com.sagunto.saguntoappmobile.data.interfaces.IAuthRepository
import com.sagunto.saguntoappmobile.data.network.provideHttpClient
import com.sagunto.saguntoappmobile.data.repository.OrderRepository
import com.sagunto.saguntoappmobile.data.repository.ProductRepository
import com.sagunto.saguntoappmobile.data.repository.UserRepository
import com.sagunto.saguntoappmobile.data.interfaces.IOrderRepository
import com.sagunto.saguntoappmobile.data.interfaces.IProductRepository
import com.sagunto.saguntoappmobile.data.interfaces.IUserRepository
import com.sagunto.saguntoappmobile.data.managers.SessionManager
import com.sagunto.saguntoappmobile.data.repository.AuthRepository
import com.sagunto.saguntoappmobile.ui.viewmodels.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::SessionManager)

    single {
        Room.databaseBuilder(
            androidContext(),
            SaguntoDatabase::class.java,
            "sagunto_database"
        ).build()
    }
    single { get<SaguntoDatabase>().orderDao() }

    single { WorkManager.getInstance(androidContext()) }


    singleOf(::AuthRepository) { bind<IAuthRepository>() }
    singleOf(::ProductRepository) { bind<IProductRepository>() }
    singleOf(::UserRepository) { bind<IUserRepository>() }
    singleOf(::OrderRepository) { bind<IOrderRepository>() }

    single { provideHttpClient(get()) }

    viewModelOf(::LoginViewModel)
    viewModelOf(::AddProductViewModel)
    viewModelOf(::AddOfflineUserViewModel)
    viewModelOf(::AddOrderViewModel)
    viewModelOf(::SelectCustomerTypeViewModel)
    viewModelOf(::UnpaidOrderViewModel)
    viewModelOf(::UserRegisterViewModel)
    viewModelOf(::UserProfileViewModel)
}