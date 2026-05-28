package com.sagunto.saguntoappmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sagunto.saguntoappmobile.ui.screens.*
import com.sagunto.saguntoappmobile.ui.theme.SaguntoAppMobileTheme
import com.sagunto.saguntoappmobile.ui.viewmodels.AddProductViewModel
import org.koin.androidx.compose.koinViewModel

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
                            LoginScreen(// es la vista donde estas
                                onLoginSuccess = { // en este caso, en caso de conseguir un login correcto nos redirecciona a la vista de MainMenuScreen
                                    //onLoginSuccess se declara en la definición de la LoginScreen como una función de callback
                                    navController.navigate("main_menu")
                                }
                            )
                        }

                        composable("main_menu") {
                            MainMenuScreen(
                                onClickNavToAddOrder = {navController.navigate("add_order")},
                                onClickNavToPayment = {navController.navigate("payment")},
                                onClickNavToAddProduct = {navController.navigate("add_product")},
                                onClickNavToCheckStatistics = {navController.navigate("check_statistics")},
                                onClickNavToAddUser = {navController.navigate("add_user")}
                            )
                        }

                        composable("add_order"){
                            AddOrderScreen()
                        }
                        composable("payment"){
                            PaymentScreen()
                        }
                        composable("add_product"){
                            val addProductViewModel = koinViewModel<AddProductViewModel>()

                            AddProductScreen(viewModel = addProductViewModel)
                        }
                        composable("check_statistics"){
                            CheckStatisticsScreen()
                        }
                        composable("add_user"){
                            AddUserScreen()
                        }
                    }
                }
            }
        }
    }
}