package com.sagunto.saguntoappmobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sagunto.saguntoappmobile.R
import com.sagunto.saguntoappmobile.ui.components.MenuOptionCard
import com.sagunto.saguntoappmobile.ui.components.StandardInputField
import com.sagunto.saguntoappmobile.ui.viewmodels.SelectCustomerTypeViewModel

// Pantalla pensada para elegir si un cliente es saguntino o no

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCustomerTypeScreen(
    navController: NavHostController,
    viewModel: SelectCustomerTypeViewModel,
    onClickToAddOrder: (Int) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val messageDialog by viewModel.messageDialog.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text(stringResource(R.string.createOrderTitle)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.btn_goBackToMenu)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF9AD99A))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xff9ad99a))
                .padding(horizontal = 24.dp), // <-- AQUÍ ESTÁ EL PADDING HOMOGÉNEO APLICADO
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Aquí sí lo mantenemos porque no hay listas que crezcan
        ) {
            StandardInputField(
                label = stringResource(R.string.txtBox_saguntino_code_label),
                placeholder = stringResource(R.string.txtBox_saguntino_code_placeholder),
                value = searchQuery,
                onValueChange = {nuevoTexto ->
                    viewModel.updateSearchQuery(nuevoTexto)
                },
                isError = !viewModel.isSaguntinoCodeValid && viewModel.isSaguntinoCodeTouched,
                errorMessage = "El nombre es obligatorio"
            )
            Spacer(modifier = Modifier.height(15.dp))
            MenuOptionCard(
                title = "SAGUNTINO",
                subtitle = "Crear pedidos para Saguntinos",
                iconResId = R.drawable.add_order,
                iconTint = Color.Green,
                iconBgColor = Color(0xFFE8F5E9),
                onClick = { viewModel.searchUsers() } //Seguramente este sea el botón "lupa"
            )
            Spacer(modifier = Modifier.height(15.dp))
            MenuOptionCard(
                title = "NO SAGUNTINO",
                subtitle = "Crear pedidos para invitados",
                iconResId = R.drawable.payment,
                iconTint = Color.Blue,
                iconBgColor = Color(0xFFE3F2FD),
                onClick = { onClickToAddOrder(-1) }
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissDialog() },
            title = { Text(messageDialog) },
            text = {
                LazyColumn(modifier = Modifier.heightIn(max = 300.dp)) {
                    items(searchResults) { user ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.dismissDialog()
                                    onClickToAddOrder(user.id)
                                }
                                .padding(16.dp)
                        ) {
                            Text(text = "${user.name} ${user.surname}", fontSize = 18.sp)
                        }
                        HorizontalDivider()
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.dismissDialog() }) {
                    Text("Cancelar")
                }
            }
        )
    }
}