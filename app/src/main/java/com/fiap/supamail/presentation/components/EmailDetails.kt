package com.fiap.supamail.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fiap.supamail.presentation.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailDetails(
    viewModel: HomeViewModel,
    navigateBack: () -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }

    val emailState = viewModel.email.collectAsState()
    val selectedEmail = emailState.value

    Scaffold(
        topBar = {
            selectedEmail?.let { email ->
                TopAppBar(
                    title = { Text(email.subject) },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            // TODO
                        }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Calendar",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = {
                            viewModel.setFavoriteInDetails(
                                email.id,
                                if (email.favorite) 0 else 1
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Favoritar",
                                tint = if (email.favorite) Color.Yellow else Color.Gray
                            )
                        }
                        IconButton(onClick = { showDialog.value = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Deletar",
                                tint = Color.Red
                            )
                        }
                    }
                )
            }
        }
    ) { padding ->
        selectedEmail?.let { email ->
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 64.dp)) {
                Text(
                    "De: ${email.sender}",
                    fontSize = 18.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    "Para: ${email.receiver}",
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    "Assunto: ${email.subject}",
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Divider(Modifier.padding(vertical = 16.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(email.body)
            }
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Atenção!") },
            text = { Text("Tem certeza que deseja deletar este email?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedEmail?.let { email ->
                            viewModel.deleteEmail(email)
                            navigateBack()
                            showDialog.value = false
                        }
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog.value = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}