package com.example.filter.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun BaseTextField (text: String, value: String,onTextChanged: (String) -> Unit){
    OutlinedTextField(
        value = value,
        onValueChange = { onTextChanged(it) },
        label = { Text(text) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color(0xFF5DB075),
            unfocusedBorderColor = Color(0xFFFFFFFF),
            cursorColor = MaterialTheme.colorScheme.primary,
            backgroundColor = Color(0xFFFFFFFF)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    )
    Divider(color = Color(0xFFE8E8E8), thickness = 1.dp)
}