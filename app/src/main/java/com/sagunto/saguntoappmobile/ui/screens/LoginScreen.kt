package com.sagunto.saguntoappmobile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sagunto.saguntoappmobile.R
import com.sagunto.saguntoappmobile.ui.theme.SaguntoSpacing

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // BOX PRINCIPAL: Necesario para que el caballo se dibuje en el fondo y el formulario encima
    Box(
        modifier = Modifier
            .fillMaxSize()
            // Se asume que has metido DarkGreenBg en tu Theme como 'background' o 'primary'
            .background(MaterialTheme.colorScheme.primary)
    ) {
        // --- EL CABALLO HA VUELTO ---
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 150.dp, y = 180.dp)
                .requiredSize(650.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.login_image_sagunto),
                contentDescription = "Decoración de fondo del caballo",
                modifier = Modifier.fillMaxSize()
            )
        }

        // --- FORMULARIO SUPERPUESTO ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = SaguntoSpacing.screenHorizontalPadding, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(120.dp))

            LoginLabel(stringResource(R.string.loginLabel_user))
            TextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text(stringResource(R.string.txtBox_user_placeholder)) },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                colors = TextFieldDefaults.colors(
                    // Se asume que tu InputBg antiguo ahora es 'surface'
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(SaguntoSpacing.medium))

            LoginLabel(stringResource(R.string.loginLabel_password))
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("........") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(SaguntoSpacing.extraLarge))

            Button(
                onClick = { onLoginSuccess() },
                // Tu 'LimeGreen' original (botón) ha sido mapeado a 'tertiary' o 'secondary' por el agente
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text(
                    stringResource(R.string.btn_log_in),
                    // Tu 'ButtonText' original (verde muy oscuro) debe estar en 'onTertiary' o puedes forzar 'primary'
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(SaguntoSpacing.small))
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun LoginLabel(text: String) {
    Text(
        text = text,
        // Usamos la opacidad dinámica sobre el color de texto principal del tema
        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = SaguntoSpacing.small, start = 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen(onLoginSuccess = {})
}