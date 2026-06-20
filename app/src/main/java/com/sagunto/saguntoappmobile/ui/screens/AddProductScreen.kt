package com.sagunto.saguntoappmobile.ui.screens

import android.R.id.input
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.sagunto.saguntoappmobile.R
import com.sagunto.saguntoappmobile.ui.components.StandardInputField
import com.sagunto.saguntoappmobile.ui.theme.SaguntoSpacing
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
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
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
                placeholder = "Introduce el nombre del producto",
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
                label = "Precio Público",
                placeholder = "Introduce el precio público",
                value = viewModel.priceGuest.value,
                onValueChange = {input ->
                    viewModel.priceGuest.value = input.replace(',', '.')
                    viewModel.isPriceGuestTouched.value = true
                },
                isError = !viewModel.isPriceGuestValid && viewModel.isPriceGuestTouched.value,
                errorMessage = "El precio debe ser un número mayor que 0 y no puede estar vacío"
            )

            Spacer(modifier = Modifier.height(SaguntoSpacing.medium))

            StandardInputField(
                label = "Precio Saguntinos",
                placeholder = "Introduce el precio para Saguntinos",
                value = viewModel.priceMember.value,
                onValueChange = {input ->
                    viewModel.priceMember.value = input.replace(',', '.')
                    viewModel.isPriceMemberTouched.value = true
                },
                isError = !viewModel.isPriceMemberValid && viewModel.isPriceMemberTouched.value,
                errorMessage = "El precio debe ser un número mayor que 0 y no puede estar vacío"
            )

            Spacer(modifier = Modifier.height(SaguntoSpacing.extraLarge))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { viewModel.saveProduct() },
                    enabled = viewModel.isFormValid
                ) {
                    Text(stringResource(R.string.btn_addProducts))
                }
            }
        }
    }
}
