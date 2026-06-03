package com.sagunto.saguntoappmobile.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sagunto.saguntoappmobile.R
import com.sagunto.saguntoappmobile.ui.components.StandardInputField
import com.sagunto.saguntoappmobile.ui.viewmodels.AddUserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserScreen(
    navController: NavHostController,
    viewModel: AddUserViewModel
) {
    val showDialog by viewModel.showCodeDialog.collectAsState()
    val saguntinoCode by viewModel.generatedSaguntinoCode.collectAsState()

    val context = LocalContext.current

    //toast de respuesta negativa
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AddUserViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //pop-up de respuesta afirmativa
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { /* ... */ },
            title = { Text("¡Usuario Creado!") },
            text = { Text("Tu código es: $saguntinoCode") },
            confirmButton = {
                Button(onClick = {
                    viewModel.dismissDialog()
                    navController.popBackStack()
                }) {
                    Text("Cerrar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(modifier = Modifier.fillMaxWidth(),
                title = { Text("Añadir Saguntino") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.btn_goBackToMenu)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF9AD99A)))
        }) {
        Column(modifier = Modifier
            .background(Color(0xff9ad99a))
            .padding(16.dp)
            .fillMaxSize())
        {
            Spacer(modifier = Modifier.height(150.dp))
            StandardInputField(
                label = "Nombre",
                placeholder = "Introduce el nombre del saguntino",
                value = viewModel.name.value,
                onValueChange = {
                    viewModel.name.value = it
                    viewModel.isNameTouched.value = true
                },
                isError = !viewModel.isNameValid && viewModel.isNameTouched.value,
                errorMessage = "El nombre es obligatorio"
            )

            Spacer(modifier = Modifier.height(20.dp))
            StandardInputField(
                label = "Apellidos",
                placeholder = "Introduce los apellidos del saguntino",
                value = viewModel.surname.value,
                onValueChange = {
                    viewModel.surname.value = it
                    viewModel.isSurnameTouched.value = true
                },
                isError = !viewModel.isNameValid && viewModel.isNameTouched.value,
                errorMessage = "Los apellidos son obligatorios"
            )

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End){
                Button(onClick = { viewModel.saveUser() },
                    enabled = viewModel.isFormValid) {
                    Text(stringResource(R.string.btn_addUser))
                }
            }
        }
    }
}