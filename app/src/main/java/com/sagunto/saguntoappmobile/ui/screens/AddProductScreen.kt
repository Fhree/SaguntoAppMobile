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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.sagunto.saguntoappmobile.R
import com.sagunto.saguntoappmobile.ui.components.StandardInputField
import com.sagunto.saguntoappmobile.ui.viewmodels.AddProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    navController: NavHostController,
    viewModel: AddProductViewModel
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AddProductViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(modifier = Modifier.fillMaxWidth(),
                title = { Text("Añadir producto") },
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
            .fillMaxSize()
            .background(Color(0xff9ad99a))
            .padding(16.dp)) {

            Spacer(modifier = Modifier.height(150.dp))
            StandardInputField(
                label = "Nombre",
                placeholder = "Introduce el nombre del producto",
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
                label = "Precio Público",
                placeholder = "Introduce el precio público",
                value = viewModel.publicPrice.value,
                onValueChange = {
                    viewModel.publicPrice.value = it
                    viewModel.isPublicPriceTouched.value = true
                },
                isError = !viewModel.isPublicPriceValid && viewModel.isPublicPriceTouched.value,
                errorMessage = "El precio debe ser un número mayor que 0 ni estar vacío"
            )

            Spacer(modifier = Modifier.height(20.dp))

            StandardInputField(
                label = "Precio Saguntinos",
                placeholder = "Introduce el precio para Saguntinos",
                value = viewModel.privatePrice.value,
                onValueChange = {
                    viewModel.privatePrice.value = it
                    viewModel.isPrivatePriceTouched.value = true
                },
                isError = !viewModel.isPrivatePriceValid && viewModel.isPrivatePriceTouched.value,
                errorMessage = "El precio debe ser un número mayor que 0 ni estar vacío"
            )

            Spacer(modifier = Modifier.height(100.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End){
                Button(onClick = { viewModel.saveProduct() },
                    enabled = viewModel.isFormValid) {
                    Text(stringResource(R.string.btn_addProducts))
                }
            }
        }
    }
}