package com.sagunto.saguntoappmobile.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign // Importante para centrar el texto
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Colores extraídos de tu diseño
private val CardBorderColor = Color(0xFF1A3824)
private val NeonGreen = Color(0xFF8CE03A)
private val TextGray = Color(0xFFA0B3A6)
private val IconTrashColor = Color(0xFFC49C96)

@Composable
fun ProductCard(
    name: String,
    price: Double,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, CardBorderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // --- FILA SUPERIOR: Nombre y Precio Unitario ---
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = name,
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${"%.2f".format(price)} €/unidad",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- FILA INFERIOR: Subtotal, Stepper y Papelera ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Lado Izquierdo: Subtotal
                Text(
                    text = "${"%.2f".format(price * quantity)} €",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                // Lado Derecho: Controles agrupados
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Selector de cantidad (Stepper)
                    Row(
                        modifier = Modifier
                            .background(Color.Gray, RoundedCornerShape(24.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onDecrease, enabled = quantity > 1) {
                            Text("-", color = TextGray, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }

                        // 🛠️ LA SOLUCIÓN: Ancho fijo y alineación central
                        Text(
                            text = quantity.toString(),
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center, // Centramos el número en su caja
                            modifier = Modifier.width(36.dp) // Reservamos espacio estático para que no se expanda
                        )

                        TextButton(onClick = onIncrease) {
                            Text("+", color = NeonGreen, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Botón de Papelera
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar producto",
                            tint = IconTrashColor
                        )
                    }
                }
            }
        }
    }
}