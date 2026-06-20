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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sagunto.saguntoappmobile.R
import com.sagunto.saguntoappmobile.data.managers.SessionManager
import com.sagunto.saguntoappmobile.ui.components.MenuOptionCard
import com.sagunto.saguntoappmobile.ui.theme.SaguntoSpacing

@Composable
fun MainMenuScreen(
    onClickNavToSelectCustomerType: () -> Unit,
    onClickNavToPayment: () -> Unit,
    onClickNavToAddProduct: () -> Unit,
    onClickNavToCheckStatistics: () -> Unit,
    onClickNavToAddUser: () -> Unit,
    onClickNavToProfile: () -> Unit,
    onClickLogout: () -> Unit,
    sessionManager: SessionManager
) {
    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(
                        horizontal = SaguntoSpacing.screenHorizontalPadding,
                        vertical = SaguntoSpacing.cardPadding
                    )
            ) {
                Button(
                    onClick = onClickLogout,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Cerrar Sesión", color = MaterialTheme.colorScheme.onError)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(horizontal = SaguntoSpacing.screenHorizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.txt_main_menu),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(SaguntoSpacing.extraLarge))

            if(sessionManager.isAdmin() || sessionManager.isBartender()) {
                MenuOptionCard(
                    title = "Consumición",
                    subtitle = "Registrar nuevas comandas",
                    iconResId = R.drawable.add_order,
                    iconTint = Color.Green,
                    iconBgColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    onClick = onClickNavToSelectCustomerType
                )

                MenuOptionCard(
                    title = "Cobrar",
                    subtitle = "Gestionar pagos pendientes",
                    iconResId = R.drawable.payment,
                    iconTint = Color.Blue,
                    iconBgColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f),
                    onClick = onClickNavToPayment
                )
            }

            MenuOptionCard(
                title = "Mi Perfil",
                subtitle = "Ver mi código saguntino",
                iconResId = R.drawable.user_profile,
                iconTint = Color.Magenta,
                iconBgColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                onClick = onClickNavToProfile
            )

            if(sessionManager.isAdmin()) {
                MenuOptionCard(
                    title = "Añadir producto",
                    subtitle = "Actualizar el inventario",
                    iconResId = R.drawable.add_product,
                    iconTint = Color.Red,
                    iconBgColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    onClick = onClickNavToAddProduct
                )
// comentada por ir justo de tiempo
//            MenuOptionCard(
//                title = "Comprobar consumo",
//                subtitle = "Ver históricos y estadísticas",
//                iconResId = R.drawable.stadistics,
//                iconTint = Color.Red,
//                iconBgColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
//                onClick = onClickNavToCheckStatistics
//            )

                MenuOptionCard(
                    title = "Añadir saguntino",
                    subtitle = "Registrar nuevo cliente VIP",
                    iconResId = R.drawable.add_user,
                    iconTint = Color.Cyan,
                    iconBgColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f),
                    onClick = onClickNavToAddUser
                )
            }
        }
    }
}
