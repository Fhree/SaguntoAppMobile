package com.sagunto.saguntoappmobile.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.sagunto.saguntoappmobile.R
import com.sagunto.saguntoappmobile.ui.components.MenuOptionCard
import com.sagunto.saguntoappmobile.ui.components.StandardInputField
import com.sagunto.saguntoappmobile.ui.theme.SaguntoSpacing
import com.sagunto.saguntoappmobile.ui.viewmodels.SelectCustomerTypeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCustomerTypeScreen(
    navController: NavHostController,
    viewModel: SelectCustomerTypeViewModel,
    onClickToAddOrder: (Boolean) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = { Text(stringResource(R.string.createOrderTitle)) },
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = SaguntoSpacing.screenHorizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MenuOptionCard(
                title = "SAGUNTINO",
                subtitle = "Crear pedidos para Saguntinos",
                iconResId = R.drawable.add_order,
                iconTint = MaterialTheme.colorScheme.tertiary,
                iconBgColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                onClick = { onClickToAddOrder(true) }
            )
            Spacer(modifier = Modifier.height(SaguntoSpacing.itemSpacing))
            MenuOptionCard(
                title = "NO SAGUNTINO",
                subtitle = "Crear pedidos para invitados",
                iconResId = R.drawable.payment,
                iconTint = MaterialTheme.colorScheme.primary,
                iconBgColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                onClick = { onClickToAddOrder(false) }
            )
        }
    }
}
