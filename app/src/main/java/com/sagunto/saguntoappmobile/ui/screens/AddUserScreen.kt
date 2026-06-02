package com.sagunto.saguntoappmobile.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sagunto.saguntoappmobile.R
import com.sagunto.saguntoappmobile.ui.components.StandardInputField
import com.sagunto.saguntoappmobile.ui.viewmodels.AddProductViewModel
import com.sagunto.saguntoappmobile.ui.viewmodels.AddUserViewModel

@Composable
fun AddUserScreen(
    viewModel: AddUserViewModel
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AddUserViewModel.UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Column(modifier = Modifier
        .background(Color(0xff9ad99a))
        .padding(16.dp))
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