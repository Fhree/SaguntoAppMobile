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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.sagunto.saguntoappmobile.R
import com.sagunto.saguntoappmobile.domain.models.Product
import com.sagunto.saguntoappmobile.ui.components.ProductCard
import com.sagunto.saguntoappmobile.ui.viewmodels.AddOrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrderScreen(
    navController: NavHostController,
    viewModel: AddOrderViewModel,
) {
    // Puedes borrar la variable productTest si ya no la usas para maquetar
    val productTest = Product(
        id = 1,
        name = "Coca-Cola",
        publicPrice = 1.5,
        privatePrice = 1.0
    )

    val productCatalog by viewModel.productCatalog.collectAsState()
    val cartItems by viewModel.cart.collectAsState()
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val totalPrice = cartItems.sumOf { it.priceSnapshot * it.quantity }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text("Añadir consumición") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.btn_goBackToMenu)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF9AD99A)
                )
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF9AD99A))
                    .padding(horizontal = 24.dp, vertical = 16.dp) // <-- APLICADO AQUÍ TAMBIÉN PARA QUE ALINEE
            ) {
                // --- TEXTO DEL TOTAL ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TOTAL:",
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Black,
                        fontSize = 20.sp,
                        color = Color(0xFF0F2617) // Verde muy oscuro
                    )
                    Text(
                        text = "${"%.2f".format(totalPrice)} €",
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Black,
                        fontSize = 24.sp,
                        color = Color(0xFF0F2617)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- BOTÓN DE CREAR PEDIDO ORIGINAL ---
                Button(
                    onClick = {
                        viewModel.saveOrder()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = cartItems.isNotEmpty()
                ) {
                    Text("CREAR PEDIDO")
                }
            }
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xff9ad99a))
                .padding(horizontal = 24.dp, vertical = 8.dp), // <-- AQUÍ ESTÁ EL PADDING HOMOGÉNEO
            horizontalAlignment = Alignment.CenterHorizontally,
            // Quitamos el verticalArrangement = Arrangement.Center para que la lista empiece arriba
        ) {
            ExposedDropdownMenuBox(
                expanded = isDropdownExpanded,
                onExpandedChange = { isDropdownExpanded = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                // El campo de texto que sirve de "botón" para abrir el menú
                OutlinedTextField(
                    value = "Seleccionar producto...", // Texto estático
                    onValueChange = {},
                    readOnly = true, // Evita que el teclado se abra
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropdownExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor() // CRÍTICO: Ancla el menú a este TextField
                        .fillMaxWidth(),
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                )

                // La lista desplegable en sí
                ExposedDropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    productCatalog.forEach { product ->
                        DropdownMenuItem(
                            text = {
                                Text("${product.name} - ${product.price}€")
                            },
                            onClick = {
                                viewModel.addProductToCart(product)
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Zona reservada para la lista del carrito (donde irán las ProductCard)
            // Quitamos padding horizontal de las tarjetas para que no se sume al de la pantalla
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(cartItems) { item ->
                    // Envolvemos la tarjeta en un Box o Column sin padding lateral extra
                    ProductCard(
                        name = item.name,
                        price = item.priceSnapshot,
                        quantity = item.quantity,
                        onIncrease = {
                            viewModel.updateQuantity(productId = item.productId, newQuantity = 1)
                        },
                        onDecrease = {
                            viewModel.updateQuantity(productId = item.productId, newQuantity = -1)
                        },
                        onDelete = {
                            viewModel.deleteProductCart(productId = item.productId)
                        }
                    )
                }
            }
        }
    }
}