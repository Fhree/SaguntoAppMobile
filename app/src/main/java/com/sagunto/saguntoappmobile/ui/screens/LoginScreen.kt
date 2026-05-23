package com.sagunto.saguntoappmobile.ui.screens // <--- UPDATED PACKAGE NAME

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.ui.res.stringResource
import com.sagunto.saguntoappmobile.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Custom Colors
val DarkGreenBg = Color(0xFF0B2918)
val LimeGreen = Color(0xFFA4E43F)
val ButtonText = Color(0xFF0B2918)
val InputBg = Color(0xFFF2F2F2)

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkGreenBg)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Star, contentDescription = "Logo", modifier = Modifier.size(60.dp), tint = DarkGreenBg)
        }

        Spacer(modifier = Modifier.height(100.dp))

        // Inputs
        LoginLabel(stringResource(R.string.loginLabel_user))
        TextField(
            value = username,
            onValueChange = { username = it },
            placeholder = { Text(stringResource(R.string.txtBox_user_placeholder)) },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBg,
                unfocusedContainerColor = InputBg,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        LoginLabel(stringResource(R.string.loginLabel_password))
        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("........") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBg,
                unfocusedContainerColor = InputBg,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onLoginSuccess() },
            colors = ButtonDefaults.buttonColors(containerColor = LimeGreen),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().height(55.dp)
        ) {
            Text(stringResource(R.string.btn_log_in), color = ButtonText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = ButtonText)
        }
    }
}

@Composable
fun LoginLabel(text: String) {
    Text(
        text = text,
        color = Color.LightGray,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp, start = 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen(onLoginSuccess = {})
}