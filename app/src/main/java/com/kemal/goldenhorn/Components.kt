package com.kemal.goldenhorn

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        color = Biobuzz.honey,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(vertical = 6.dp)
    )
}

@Composable
fun BioCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Biobuzz.bgCard),
        border = BorderStroke(1.dp, Biobuzz.honey.copy(alpha = 0.15f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(14.dp), content = content)
    }
}

@Composable
fun BioTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    numeric: Boolean = false,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = if (numeric)
            KeyboardOptions(keyboardType = KeyboardType.Number) else KeyboardOptions.Default,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Biobuzz.honey,
            unfocusedBorderColor = Biobuzz.mutedG.copy(alpha = 0.4f),
            focusedLabelColor = Biobuzz.honey,
            unfocusedLabelColor = Biobuzz.mutedG,
            focusedTextColor = Biobuzz.cream,
            unfocusedTextColor = Biobuzz.cream,
            cursorColor = Biobuzz.honey
        ),
        modifier = modifier.fillMaxWidth()
    )
}
