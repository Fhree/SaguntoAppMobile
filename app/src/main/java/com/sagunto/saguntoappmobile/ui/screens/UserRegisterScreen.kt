package com.sagunto.saguntoappmobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
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

        OutlinedTextField(
            value = name,
            onValueChange = { viewModel.name.value = it },
            label = { Text("Nombre *") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = surname,
            onValueChange = { viewModel.surname.value = it },
            label = { Text("Apellidos (Opcional)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.email.value = it },
            label = { Text("Correo Electrónico *") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.password.value = it },
            label = { Text("Contraseña *") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
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