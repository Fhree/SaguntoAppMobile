package com.sagunto.saguntoappmobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sagunto.saguntoappmobile.R
import com.sagunto.saguntoappmobile.ui.viewmodels.AddProductViewModel

@Composable
fun AddProductScreen(
    viewModel: AddProductViewModel
) {
    Column(modifier = Modifier
        .background(Color(0xff9ad99a))
        .padding(16.dp)) {

        // Input Nombre
        TextField(
            value = viewModel.name.value,
            onValueChange = { viewModel.name.value = it }, // Actualizamos el ViewModel
            label = { Text("Nombre del Producto") }
        )

        // Input Precio Público
        TextField(
            value = viewModel.publicPrice.value,
            onValueChange = { viewModel.publicPrice.value = it },
            label = { Text("Precio Público") }
        )

        // Input Precio Privado
        TextField(
            value = viewModel.privatePrice.value,
            onValueChange = { viewModel.privatePrice.value = it },
            label = { Text("Precio Privado") }
        )

        Button(onClick = { viewModel.saveProduct() }) {
            Text(stringResource(R.string.btn_addProducts))
        }
    }
}