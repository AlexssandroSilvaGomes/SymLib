package br.senai.sp.jandira.symlib.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.senai.sp.jandira.symlib.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputText(
    text: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = text,
        onValueChange = { onValueChange(it) },
        modifier = Modifier
            .width(400.dp)
            .height(62.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.White,
            errorBorderColor = Color.Transparent,
            cursorColor = Color(65, 57, 70, 255)
        ),
        shape = RoundedCornerShape(20.dp),
        placeholder = { Text(placeholder, fontSize = 18.sp, color = Color.Black) },
        textStyle = TextStyle.Default.copy(fontSize = 15.sp, color = Color.Black),
    )
}

@Preview(showBackground = false)
@Composable
fun PreviewInputText() {
    InputText(text = "Insira seu email", placeholder = "email", onValueChange = {})
}