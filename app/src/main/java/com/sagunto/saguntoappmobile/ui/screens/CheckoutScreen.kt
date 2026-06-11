package com.sagunto.saguntoappmobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.sagunto.saguntoappmobile.R
import com.sagunto.saguntoappmobile.ui.components.StandardInputField
import com.sagunto.saguntoappmobile.ui.components.UnpaidOrderCard
import com.sagunto.saguntoappmobile.ui.theme.SaguntoSpacing
import com.sagunto.saguntoappmobile.ui.viewmodels.CheckoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavHostController,
    viewModel: CheckoutViewModel
) {
    val showResultDialog by viewModel.showResultDialog.collectAsState()
    val messageDialog by viewModel.messageDialog.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val unpaidOrders by viewModel.unpaidOrders.collectAsState()
    val totalPrice by viewModel.unpaidOrdersTotalPrice.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val showSearchDialog by viewModel.showSearchDialog.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    val customer by viewModel.customer.collectAsState()
    val isPaymentSuccess by viewModel.isPaymentSuccess.collectAsState()

    var showConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Liquidar Deudas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            if (unpaidOrders.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(SaguntoSpacing.screenHorizontalPadding, SaguntoSpacing.cardPadding)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TOTAL A COBRAR:",
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "${"%.2f".format(totalPrice)} €",
                            fontWeight = FontWeight.Black,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(SaguntoSpacing.medium))

                    Button(
                        onClick = { showConfirmDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        Text(if (isLoading) "PROCESANDO..." else "LIQUIDAR DEUDA COMPLETA")
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = SaguntoSpacing.screenHorizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(SaguntoSpacing.small))

            // --- BUSCADOR ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    StandardInputField(
                        label = stringResource(R.string.txtBox_saguntino_code_label),
                        placeholder = stringResource(R.string.txtBox_saguntino_code_placeholder),
                        value = searchQuery,
                        onValueChange = { viewModel.updateSearchQuery(it) },
                        isError = false,
                        errorMessage = ""
                    )
                }

                Button(
                    modifier = Modifier.height(56.dp),
                    shape = MaterialTheme.shapes.medium,
                    onClick = { viewModel.executeSearch() }
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Buscar")
                }
            }

            customer?.let { selectedCustomer ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(SaguntoSpacing.small),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${selectedCustomer.name} ${selectedCustomer.surname}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = selectedCustomer.saguntinoCode,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(SaguntoSpacing.medium))


            // --- LISTADO DE PEDIDOS ---
            if (unpaidOrders.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No hay consumiciones pendientes\npara este código.",
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(SaguntoSpacing.small)
                ) {
                    items(unpaidOrders) { order ->
                        UnpaidOrderCard(order)
                    }
                }
            }
        }
    }

    // --- DIÁLOGO DE CONFIRMACIÓN ---
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirmar Cobro") },
            text = { Text("¿Confirmas que el cliente entrega el importe de ${"%.2f".format(totalPrice)} € para liquidar todas sus deudas?") },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        viewModel.processPayment()
                    }
                ) {
                    Text("Sí, liquidar deuda")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) { Text("Cancelar") }
            }
        )
    }

    // --- DIÁLOGO DE RESULTADO DEL SERVIDOR ---
    if (showResultDialog) {
        AlertDialog(
            onDismissRequest = { },
            // title = { Text(if (isPaymentSuccess == true) "Éxito" else "Error") },
            title = { Text("Información") },
            text = { Text(messageDialog) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.dismissResultDialog()
                        // 🛠️ Aquí añades el if (isPaymentSuccess == true) para volver al menú
                        // navController.popBackStack("main_menu", inclusive = false)
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }

    if (showSearchDialog) {
        AlertDialog(
        onDismissRequest = { /* Aquí deberías llamar a un método del VM para cerrar, ej: viewModel.dismissSearchDialog() */ },
            title = { Text("Seleccionar Saguntino") },
            text = {
                if (searchResults.isEmpty()) {
                    Text("No se encontraron usuarios.")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(searchResults) { user ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { viewModel.selectCustomer(user) }
                                ) {
                                    Text(
                                        text = "${user.name} ${user.surname} (${user.saguntinoCode})",
                                        modifier = Modifier.padding(SaguntoSpacing.cardPadding),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { /* Lógica para cerrar el diálogo */ }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}