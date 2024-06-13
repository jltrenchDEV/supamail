package com.fiap.supamail.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.fiap.supamail.data.entity.EmailEntity
import com.fiap.supamail.presentation.HomeViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EmailCard(
    email: EmailEntity, homeViewModel: HomeViewModel, modifier: Modifier, onEmailClick: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    val onCheckedChange: (value: EmailEntity) -> Unit = remember {
        return@remember homeViewModel::updateEmail
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Excluir email") },
            text = { Text("Você tem certeza que deseja excluir este email?") },
            confirmButton = {
                TextButton(onClick = {
                    homeViewModel.deleteEmail(email)
                    showDialog = false
                }) {
                    Text("Sim")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Não")
                }
            }
        )
    }
    Card(
        modifier = modifier
            .padding(16.dp)
            .combinedClickable(
                enabled = true,
                onClick = { onEmailClick() },
                onLongClick = { showDialog = true })
    ) {
        Row(
            modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .background(if (email.alreadyOpened) Color.LightGray else Color.White)
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .weight(3f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = if (email.alreadyOpened) "${email.subject} (Lido)" else email.subject,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = email.sentDate,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = email.body,
                    maxLines = 2,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                IconButton(onClick = {
                    onCheckedChange(
                        EmailEntity(
                            id = email.id,
                            sender = email.sender,
                            subject = email.subject,
                            body = email.body,
                            alreadyOpened = email.alreadyOpened,
                            favorite = !email.favorite,
                            sentDate = email.sentDate
                        )
                    )
                }) {
                    if (email.favorite) {
                        Icon(
                            Icons.Filled.Star, contentDescription = "Favorito", tint = Color.Yellow
                        )
                    } else {
                        Icon(
                            Icons.Outlined.Star,
                            contentDescription = "Não favorito",
                            tint = Color.Gray
                        )
                    }
                }
            }
        }
    }
}