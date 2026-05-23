package com.sagunto.saguntoappmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sagunto.saguntoappmobile.ui.screens.*
import com.sagunto.saguntoappmobile.ui.theme.SaguntoAppMobileTheme

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
                            LoginScreen(// se la vista donde estas
                                onLoginSuccess = { // en este caso, en caso de conseguir un login correcto nos redirecciona a la vista de MainMenuScreen
                                    navController.navigate("main_menu")
                                }
                            )
                        }

                        composable("main_menu") {
                            MainMenuScreen()
                        }
                    }
                }
            }
        }
    }
}