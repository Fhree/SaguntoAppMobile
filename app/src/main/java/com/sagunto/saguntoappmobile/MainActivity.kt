package com.sagunto.saguntoappmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sagunto.saguntoappmobile.ui.screens.*
import com.sagunto.saguntoappmobile.ui.theme.SaguntoAppMobileTheme
import com.sagunto.saguntoappmobile.ui.viewmodels.AddOrderViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.AddProductViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.AddUserViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.SelectCustomerTypeViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

class MainActivity : ComponentActivity() {
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
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = {
                                    navController.navigate("main_menu")
                                }
                            )
                        }

                        composable("main_menu") {
                            MainMenuScreen(
                                onClickNavToSelectCustomerType = {navController.navigate("select_customer_type")},
                                onClickNavToPayment = {navController.navigate("payment")},
                                onClickNavToAddProduct = {navController.navigate("add_product")},
                                onClickNavToCheckStatistics = {navController.navigate("check_statistics")},
                                onClickNavToAddUser = {navController.navigate("add_user")}
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
                            arguments = listOf(
                                navArgument("id") { type = NavType.IntType },
                            )
                        ){ backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("id") ?: -1

                            AddOrderScreen(navController,
                                viewModel = koinViewModel<AddOrderViewModel>(parameters = { parametersOf(id) }))
                        }
                        composable("payment"){
                            PaymentScreen()
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