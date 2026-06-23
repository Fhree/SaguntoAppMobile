package com.sagunto.saguntoappmobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
// 🛠️ CRÍTICO: Asegúrate de importar tu componente
import com.sagunto.saguntoappmobile.ui.components.StandardInputField
import com.sagunto.saguntoappmobile.ui.viewmodels.UserRegisterViewModel

@Composable
fun UserRegisterScreen(
    viewModel: UserRegisterViewModel,
    onNavigateToHome: () -> Unit
) {
    val name by viewModel.name.collectAsState()
    val surname by viewModel.surname.collectAsState()
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()

    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val successCode by viewModel.successCode.collectAsState()

    var showSuccessDialog by remember { mutableStateOf(false) }
    var saguntinoCodeResult by remember { mutableStateOf("") }

    LaunchedEffect(successCode) {
        if (successCode != null) {
            saguntinoCodeResult = successCode!!
            showSuccessDialog = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Registro de Saguntino", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        // 🛠️ Aplicado StandardInputField
        StandardInputField(
            value = name,
            onValueChange = { viewModel.name.value = it },
            label = "Nombre",
            placeholder = "Introduce tu nombre"
        )
        Spacer(modifier = Modifier.height(8.dp))

        StandardInputField(
            value = surname,
            onValueChange = { viewModel.surname.value = it },
            label = "Apellidos (Opcional)",
            placeholder = "Introduce tus apellidos"
        )
        Spacer(modifier = Modifier.height(8.dp))

        StandardInputField(
            value = email,
            onValueChange = { viewModel.email.value = it },
            label = "Correo Electrónico",
            placeholder = "tu@email.com"
        )
        Spacer(modifier = Modifier.height(8.dp))

        // ⚠️ ADVERTENCIA: Revisa que tu StandardInputField soporte enmascarar contraseñas
        StandardInputField(
            value = password,
            onValueChange = { viewModel.password.value = it },
            label = "Contraseña",
            placeholder = "••••••••"
        )
        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { viewModel.register() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrarse")
            }
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error
            )
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("¡Bienvenido/a a Sagunto!") },
            text = { Text("Registro completado con éxito.\n\nTu Código Saguntino es: $saguntinoCodeResult\n\nGuárdalo bien, lo necesitarás.") },
            confirmButton = {
                Button(onClick = {
                    showSuccessDialog = false
                    viewModel.resetSuccessState()
                    onNavigateToHome()
                }) {
                    Text("Entrar a la app")
                }
            }
        )
    }
}