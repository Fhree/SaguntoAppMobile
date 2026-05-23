package com.sagunto.saguntoappmobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sagunto.saguntoappmobile.R
import com.sagunto.saguntoappmobile.ui.components.MenuOptionCard

val LightGreenIconBg = Color(0xFFE8F5E9)
val LightBlueIconBg = Color(0xFFE3F2FD)

@Composable
fun MainMenuScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Text(
            text = stringResource(R.string.txt_main_menu),
            color = DarkGreenBg,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(100.dp))

        MenuOptionCard(
            title = "Consumición",
            subtitle = "Registrar nuevas comandas",
            icon = Icon,
            iconTint = Color.Green,
            iconBgColor = LightGreenIconBg,
            onClick = { /* TODO: Navegar a Consumición */ }
        )

        MenuOptionCard(
            title = "Cobrar",
            subtitle = "Gestionar pagos pendientes",
            icon = Icons.Default.ShoppingCart,
            iconTint = Color.Blue,
            iconBgColor = LightBlueIconBg,
            onClick = { /* TODO: Navegar a Cobrar */ }
        )

        MenuOptionCard(
            title = "Añadir producto",
            subtitle = "Actualizar el inventario",
            icon = Icons.Default.AddCircle,
            iconTint = Color.Red,
            iconBgColor = LightGreenIconBg,
            onClick = { /* TODO: Navegar a Añadir producto */ }
        )

        MenuOptionCard(
            title = "Añadir saguntino",
            subtitle = "Registrar nuevo cliente VIP",
            icon = Icons.Default.Person,
            iconTint = Color.Cyan,
            iconBgColor = LightBlueIconBg,
            onClick = { /* TODO: Navegar a Añadir saguntino */ }
        )
    }
}