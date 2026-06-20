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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sagunto.saguntoappmobile.R
import com.sagunto.saguntoappmobile.ui.components.StandardInputField
import com.sagunto.saguntoappmobile.ui.theme.SaguntoSpacing
import com.sagunto.saguntoappmobile.ui.viewmodels.AddOfflineUserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddUserScreen(
    navController: NavHostController,
    viewModel: AddOfflineUserViewModel
) {
    val showDialog by viewModel.showCodeDialog.collectAsState()
    val saguntinoCode by viewModel.generatedSaguntinoCode.collectAsState()
    val selectedRole by viewModel.selectedRole.collectAsState()
    
    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AddOfflineUserViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    
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
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
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
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = SaguntoSpacing.screenHorizontalPadding)
        ) {
            Spacer(modifier = Modifier.height(SaguntoSpacing.extraLarge))
            
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

            Spacer(modifier = Modifier.height(SaguntoSpacing.medium))
            
            StandardInputField(
                label = "Apellidos",
                placeholder = "Introduce los apellidos del saguntino",
                value = viewModel.surname.value,
                onValueChange = {
                    viewModel.surname.value = it
                    viewModel.isSurnameTouched.value = true
                },
                isError = !viewModel.isSurnameValid && viewModel.isSurnameTouched.value,
                errorMessage = "Los apellidos son obligatorios"
            )

            Spacer(modifier = Modifier.height(SaguntoSpacing.medium))
            
//            // Dropdown de Roles
//            Text(
//                text = "Rol del usuario",
//                color = MaterialTheme.colorScheme.primary,
//                fontSize = 14.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//
//            ExposedDropdownMenuBox(
//                expanded = expanded,
//                onExpandedChange = { expanded = !expanded },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                OutlinedTextField(
//                    value = selectedRole.name,
//                    onValueChange = {},
//                    readOnly = true,
//                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//                    modifier = Modifier
//                        .menuAnchor()
//                        .fillMaxWidth(),
//                    colors = OutlinedTextFieldDefaults.colors(
//                        focusedContainerColor = MaterialTheme.colorScheme.surface,
//                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
//                    ),
//                    shape = RoundedCornerShape(16.dp)
//                )
//
//                ExposedDropdownMenu(
//                    expanded = expanded,
//                    onDismissRequest = { expanded = false }
//                ) {
//                    viewModel.roles.forEach { role ->
//                        DropdownMenuItem(
//                            text = { Text(role.name) },
//                            onClick = {
//                                viewModel.selectRole(role)
//                                expanded = false
//                            }
//                        )
//                    }
//                }
//            }

            Spacer(modifier = Modifier.height(SaguntoSpacing.extraLarge))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { viewModel.saveUser() },
                    enabled = viewModel.isFormValid
                ) {
                    Text(stringResource(R.string.btn_addUser))
                }
            }
        }
    }
}
