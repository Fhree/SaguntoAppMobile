package com.sagunto.saguntoappmobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sagunto.saguntoappmobile.R
import com.sagunto.saguntoappmobile.ui.components.ProductCard
import com.sagunto.saguntoappmobile.ui.theme.SaguntoSpacing
import com.sagunto.saguntoappmobile.ui.viewmodels.AddOrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrderScreen(
    navController: NavHostController,
    viewModel: AddOrderViewModel,
) {
    val productCatalog by viewModel.productCatalog.collectAsState()
    val cartItems by viewModel.cart.collectAsState()

    // 🛠️ Escuchamos los estados de respuesta del servidor
    val showResultDialog by viewModel.showResultDialog.collectAsState()
    val messageDialog by viewModel.messageDialog.collectAsState()
    val isOrderSuccess by viewModel.isOrderSuccess.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var isDropdownExpanded by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }

    val totalPrice = cartItems.sumOf { it.priceSnapshot * it.quantity }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text("Añadir consumición" ) },
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
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = SaguntoSpacing.screenHorizontalPadding, vertical = SaguntoSpacing.cardPadding)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TOTAL:",
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "${"%.2f".format(totalPrice)} €",
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                Spacer(modifier = Modifier.height(SaguntoSpacing.medium))

                Button(
                    onClick = { showPaymentDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    // 🛠️ Bloqueamos el botón si la lista está vacía o el servidor está cargando la petición
                    enabled = cartItems.isNotEmpty() && !isLoading
                ) {
                    Text(if (isLoading) "CREANDO..." else "CREAR PEDIDO")
                }
            }
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = SaguntoSpacing.screenHorizontalPadding, vertical = SaguntoSpacing.small),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = "Seleccionar producto...",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                        .fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors()
                )

                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    productCatalog.forEach { product ->
                        DropdownMenuItem(
                            text = { Text("${product.name} - ${product.price}€") },
                            onClick = {
                                viewModel.addProductToCart(product)
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(SaguntoSpacing.large))

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(cartItems) { item ->
                    ProductCard(
                        name = item.name,
                        price = item.priceSnapshot,
                        quantity = item.quantity,
                        onIncrease = { viewModel.updateQuantity(productId = item.productId, newQuantity = 1) },
                        onDecrease = { viewModel.updateQuantity(productId = item.productId, newQuantity = -1) },
                        onDelete = { viewModel.deleteProductCart(productId = item.productId) }
                    )
                }
            }
        }
    }

    // --- 1. DIÁLOGOS DE CONFIRMACIÓN DE PAGO (LOCALES) ---
    if (showPaymentDialog) {
        if (viewModel.isSaguntino) {
            AlertDialog(
                onDismissRequest = { showPaymentDialog = false },
                title = { Text("Confirmación (Saguntino)") },
                text = { Text("¿El cliente ha abonado la consumición en este momento?") },
                confirmButton = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                showPaymentDialog = false
                                // 🛠️ Solo enviamos a .NET. No navegamos.
                                viewModel.saveOrder(isPaid = true)
                            }
                        ) {
                            Text("Sí, está pagado")
                        }

                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            onClick = {
                                showPaymentDialog = false
                                viewModel.saveOrder(isPaid = false)
                            }
                        ) {
                            Text("No, dejar pendiente")
                        }

                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { showPaymentDialog = false }
                        ) {
                            Text("Cancelar y seguir editando", textAlign = TextAlign.Center)
                        }
                    }
                }
            )
        } else {
            AlertDialog(
                onDismissRequest = { showPaymentDialog = false },
                title = { Text("Confirmación (Invitado)") },
                text = { Text("Los usuarios no saguntinos deben abonar la consumición en el momento de pedir.\n\n¿Confirmar creación y cobro del pedido?") },
                confirmButton = {
                    Button(
                        onClick = {
                            showPaymentDialog = false
                            viewModel.saveOrder(isPaid = true)
                        }
                    ) {
                        Text("Confirmar y cobrar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showPaymentDialog = false }) { Text("Cancelar") }
                }
            )
        }
    }

    // --- 2. DIÁLOGO DE RESULTADO FINAL (REACTIVO DEL SERVIDOR) ---
    if (showResultDialog) {
        AlertDialog(
            // Obligamos al usuario a pulsar el botón para cerrar, no tocando fuera
            onDismissRequest = {  },
            title = {
                Text(if (isOrderSuccess == true) "Operación exitosa" else "Error")
            },
            text = { Text(messageDialog) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.dismissResultDialog()
                        // 🛠️ Solo navegamos si la petición al servidor fue un 200 OK
                        if (isOrderSuccess == true) {
                            navController.popBackStack("main_menu", inclusive = false)
                        }
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}