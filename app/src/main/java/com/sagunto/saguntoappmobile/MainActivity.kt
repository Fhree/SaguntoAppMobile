package com.sagunto.saguntoappmobile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.security.ProviderInstaller
import com.sagunto.saguntoappmobile.data.interfaces.IAuthRepository
import com.sagunto.saguntoappmobile.data.managers.SessionManager
import com.sagunto.saguntoappmobile.ui.screens.*
import com.sagunto.saguntoappmobile.ui.theme.SaguntoAppMobileTheme
import com.sagunto.saguntoappmobile.ui.viewmodels.AddOrderViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.AddProductViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.AddOfflineUserViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.LoginViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.SelectCustomerTypeViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.UnpaidOrderViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.UserRegisterViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.UserProfileViewModel
import com.sagunto.saguntoappmobile.workers.SyncProductsWorker
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.util.concurrent.TimeUnit
import com.sagunto.saguntoappmobile.workers.SyncUsersWorker

class MainActivity : ComponentActivity() {

    private val authRepository: IAuthRepository by inject()
    private val sessionManager: SessionManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {

        try {
            ProviderInstaller.installIfNeeded(this)
            Log.i("SECURITY", "TLS Provider actualizado correctamente")
        } catch (e: Exception) {
            Log.e("SECURITY", "Fallo al actualizar el proveedor TLS", e)
        }

        super.onCreate(savedInstanceState)

        // 🛠️ Registramos el worker para que sincronice en segundo plano
        setupBackgroundSync(this)

        // enableEdgeToEdge()
        setContent {
            SaguntoAppMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val currentUser by sessionManager.currentUser.collectAsState()
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
                                onClickNavToProfile = {navController.navigate("user_profile")},
                                onClickLogout = {
                                    authRepository.logout()
                                    sessionManager.clearSession()
                                    navController.navigate("login") {
                                        popUpTo(0) { inclusive = true }
                                    }
                                },
                                sessionManager = sessionManager
                            )
                        }

                        composable("user_profile") {
                            UserProfileScreen(
                                viewModel = koinViewModel<UserProfileViewModel>(),
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable("select_customer_type") {
                            SelectCustomerTypeScreen(
                                navController = navController,
                                viewModel = koinViewModel<SelectCustomerTypeViewModel>(),
                                onClickToAddOrder = { isSaguntino ->
                                    navController.navigate("add_order/$isSaguntino")
                                }
                            )
                        }
                        composable(route = "add_order/{isSaguntino}",
                            arguments = listOf(navArgument("isSaguntino") { type = NavType.BoolType })
                        ){ backStackEntry ->
                            val isSaguntino = backStackEntry.arguments?.getBoolean("isSaguntino") ?: false

                            AddOrderScreen(
                                navController = navController,
                                viewModel = koinViewModel<AddOrderViewModel>(
                                    parameters = { parametersOf(isSaguntino) }
                                )
                            )
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
                            AddUserScreen(navController,viewModel = koinViewModel<AddOfflineUserViewModel>())
                        }
                    }
                }
            }
        }
    }

    private fun setupBackgroundSync(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Cambiado a CONNECTED: UNMETERED ignora Wi-Fis marcadas como medidas o datos móviles
            .build()

        val workManager = WorkManager.getInstance(context)

        // --- PRODUCTOS ---
        val periodicSyncProducts = PeriodicWorkRequestBuilder<SyncProductsWorker>(12, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "PeriodicSyncProductsWork",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicSyncProducts
        )

        val immediateSyncProducts = OneTimeWorkRequestBuilder<SyncProductsWorker>().build()
        workManager.enqueue(immediateSyncProducts)

        // --- USUARIOS SAGUNTINOS ---
        val periodicSyncUsers = PeriodicWorkRequestBuilder<SyncUsersWorker>(12, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "PeriodicSyncUsersWork",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicSyncUsers
        )

        val immediateSyncUsers = OneTimeWorkRequestBuilder<SyncUsersWorker>().build()
        workManager.enqueue(immediateSyncUsers)
    }
}