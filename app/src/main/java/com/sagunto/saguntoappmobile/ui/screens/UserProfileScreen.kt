package com.sagunto.saguntoappmobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sagunto.saguntoappmobile.ui.theme.SaguntoSpacing
import com.sagunto.saguntoappmobile.ui.viewmodels.UserProfileViewModel

@Composable
fun UserProfileScreen(
    viewModel: UserProfileViewModel,
    onNavigateBack: () -> Unit
) {
    val saguntinoCode by viewModel.saguntinoCode.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(SaguntoSpacing.screenHorizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título descriptivo
        Text(
            text = "Tu Código Saguntino:",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        // Valor del código resaltado
        Text(
            text = saguntinoCode,
            style = MaterialTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 4.sp
            ),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = SaguntoSpacing.medium)
        )

        Spacer(modifier = Modifier.height(SaguntoSpacing.medium))

        // Botón de Volver
        Button(
            onClick = onNavigateBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        ) {
            Text(
                text = "Volver",
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 18.sp)
            )
        }
    }
}
