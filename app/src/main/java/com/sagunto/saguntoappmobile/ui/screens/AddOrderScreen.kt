package com.sagunto.saguntoappmobile.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun AddOrderScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Estás en Consumición", fontSize = 24.sp)
    }
}

// Crea también estos archivos siguiendo la misma estructura:
// - PayOrderScreen.kt ("Estás en Cobrar")
// - AddProductScreen.kt ("Estás en Añadir Producto")
// - CheckConsumptionScreen.kt ("Estás en Comprobar Consumo")
// - AddSaguntinoScreen.kt ("Estás en Añadir Saguntino")