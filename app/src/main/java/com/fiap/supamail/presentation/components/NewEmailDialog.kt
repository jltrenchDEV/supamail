package com.fiap.supamail.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.fiap.supamail.data.entity.EmailEntity
import com.fiap.supamail.presentation.HomeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewEmailDialog(homeViewModel: HomeViewModel, onDialogClose: () -> Unit) {
    val defaultEmail = EmailEntity(
        sender = "Você",
        sentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
        alreadyOpened = false,
        favorite = false,
    )
    var email by remember { mutableStateOf(defaultEmail) }
    var isFormValid by remember { mutableStateOf(false) }

    LaunchedEffect(email) {
        isFormValid =
            email.receiver.isNotBlank() && email.subject.isNotBlank() && email.body.isNotBlank()
    }

    AlertDialog(
        onDismissRequest = onDialogClose,
        title = { Text("Enviar Email") },
        text = {
            Column {
                OutlinedTextField(
                    value = email.receiver,
                    onValueChange = { email = email.copy(receiver = it) },
                    placeholder = { Text("Destinatário") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(15.dp))
                OutlinedTextField(
                    value = email.subject,
                    onValueChange = { email = email.copy(subject = it) },
                    placeholder = { Text("Titulo do Email") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(15.dp))
                OutlinedTextField(
                    value = email.body,
                    onValueChange = { email = email.copy(body = it) },
                    placeholder = { Text("Corpo do Email") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(),
                    maxLines = 6
                )
            }
        },
        confirmButton = {
            OutlinedButton(onClick = {
                if (isFormValid) {
                    homeViewModel.insertEmail(email)
                    onDialogClose()
                } else {
                    // Mostrar mensagem de erro
                }
            }) {
                Text("Criar Email")
            }
        }
    )
}