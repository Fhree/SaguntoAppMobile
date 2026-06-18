package com.sagunto.saguntoappmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sagunto.saguntoappmobile.data.interfaces.IAuthRepository
import com.sagunto.saguntoappmobile.ui.screens.*
import com.sagunto.saguntoappmobile.ui.theme.SaguntoAppMobileTheme
import com.sagunto.saguntoappmobile.ui.viewmodels.AddOrderViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.AddProductViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.AddUserViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.LoginViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.SelectCustomerTypeViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.UnpaidOrderViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.UserRegisterViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val authRepository: IAuthRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SaguntoAppMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val currentUser by authRepository.currentUser.collectAsState()

                    LaunchedEffect(currentUser) {
                        if (currentUser != null) {
                            navController.navigate("main_menu") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }

                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                viewModel = koinViewModel<LoginViewModel>(),
                                onNavigateToRegister = { navController.navigate("register") }
                            )
                        }

                        composable("register") {
                            UserRegisterScreen(
                                viewModel = koinViewModel<UserRegisterViewModel>(),
                                onNavigateToHome = {
                                    navController.navigate("main_menu") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("main_menu") {
                            MainMenuScreen(
                                onClickNavToSelectCustomerType = {navController.navigate("select_customer_type")},
                                onClickNavToPayment = {navController.navigate("checkout")},
                                onClickNavToAddProduct = {navController.navigate("add_product")},
                                onClickNavToCheckStatistics = {navController.navigate("check_statistics")},
                                onClickNavToAddUser = {navController.navigate("add_user")},
                                onClickLogout = {
                                    authRepository.logout()

                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable("select_customer_type") {
                            SelectCustomerTypeScreen(
                                navController = navController,
                                viewModel = koinViewModel<SelectCustomerTypeViewModel>(),
                                onClickToAddOrder = { id ->
                                    navController.navigate("add_order/$id")
                                }
                            )
                        }
                        composable(
                            "add_order/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ){ backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("id") ?: -1

                            AddOrderScreen(navController,
                                viewModel = koinViewModel<AddOrderViewModel>(parameters = { parametersOf(id) }))
                        }
                        composable("checkout"){
                            UnpaidOrderScreen(navController, viewModel = koinViewModel<UnpaidOrderViewModel>())
                        }
                        composable("add_product"){
                            AddProductScreen(navController,viewModel = koinViewModel<AddProductViewModel>())
                        }
                        composable("check_statistics"){
                            CheckStatisticsScreen()
                        }
                        composable("add_user"){
                            AddUserScreen(navController,viewModel = koinViewModel<AddUserViewModel>())
                        }
                    }
                }
            }
        }
    }
}