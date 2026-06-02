package com.sagunto.saguntoappmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.draw.alpha
import com.sagunto.saguntoappmobile.ui.screens.DarkGreenBg
import com.sagunto.saguntoappmobile.ui.screens.InputBg

@Composable
fun StandardInputField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    Column() {

        Text(
            text = label,
            color = DarkGreenBg,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = InputBg,
                unfocusedContainerColor = InputBg,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                errorContainerColor = InputBg
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth().then(
                if (isError) Modifier.border(2.dp, Color.Red, RoundedCornerShape(16.dp))
                else Modifier
            ),
            isError = isError,
        )

        ErrorLabel(
            text = errorMessage,
            modifier = Modifier.alpha(if (isError) 1f else 0f)
        )
    }
}

@Composable
fun ErrorLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = Color.Red,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        modifier = modifier.padding(bottom = 8.dp, start = 4.dp)
    )
}


