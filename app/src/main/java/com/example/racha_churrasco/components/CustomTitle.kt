package com.example.racha_churrasco.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomTitle(text: String) {
    Text(
        text=text,
        fontSize = 58.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
}