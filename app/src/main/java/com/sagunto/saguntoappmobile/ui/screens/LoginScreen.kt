package com.sagunto.saguntoappmobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sagunto.saguntoappmobile.ui.theme.SaguntoSpacing

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit = {}) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary) // Verde oscuro de fondo
            .padding(SaguntoSpacing.screenHorizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Star, 
                contentDescription = "Logo", 
                modifier = Modifier.size(60.dp), 
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(SaguntoSpacing.extraLarge))

        Text(
            "Bienvenido", 
            color = MaterialTheme.colorScheme.onPrimary, 
            fontSize = 28.sp, 
            fontWeight = FontWeight.Bold
        )
        Text(
            "Gestión de bar profesional", 
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f), 
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Inputs
        LoginLabel("USUARIO")
        TextField(
            value = username,
            onValueChange = { username = it },
            placeholder = { Text("Introduce tu usuario") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(SaguntoSpacing.medium))

        LoginLabel("CONTRASEÑA")
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

        // Button
        Button(
            onClick = onLoginSuccess,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(55.dp)
        ) {
            Text(
                "Acceder", 
                color = MaterialTheme.colorScheme.primary, 
                fontSize = 18.sp, 
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(SaguntoSpacing.small))
            Icon(
                Icons.Default.ArrowForward, 
                contentDescription = null, 
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun LoginLabel(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth().padding(bottom = SaguntoSpacing.small, start = 4.dp)
    )
}
