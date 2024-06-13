package com.fiap.supamail.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
    Scaffold(
        topBar = {
            val email = viewModel.email.collectAsState().value
            email?.let {
                TopAppBar(
                    title = { Text(it.subject) },
                    navigationIcon = {
                        IconButton(onClick = navigateBack) {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    actions = {
                        val emailState = viewModel.email.collectAsState()
                        val selectedEmail = emailState.value

                        IconButton(onClick = {
                            viewModel.setFavoriteInDetails(
                                selectedEmail!!.id,
                                if (selectedEmail.favorite) 0 else 1
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Favoritar",
                                tint = if (emailState.value?.favorite == true) Color.Yellow else Color.Gray
                            )
                        }
                        IconButton(onClick = {
                            viewModel.deleteEmail(selectedEmail!!)
                            navigateBack()
                        }) {
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
        val email = viewModel.email.collectAsState().value
        email?.let {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 64.dp)) {
                Text(
                    "De: ${it.sender}",
                    fontSize = 18.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    "Para: ${it.receiver}",
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    "Assunto: ${it.subject}",
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Divider(Modifier.padding(vertical = 16.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(it.body)
            }
        }
    }
}