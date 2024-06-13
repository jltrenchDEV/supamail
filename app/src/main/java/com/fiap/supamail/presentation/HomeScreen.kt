@file:OptIn(ExperimentalMaterial3Api::class)

package com.fiap.supamail.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.fiap.supamail.data.entity.EmailEntity
import com.fiap.supamail.presentation.components.EmailCard
import com.fiap.supamail.presentation.components.NewEmailDialog
import com.fiap.supamail.presentation.components.SearchEmailBar

@Composable
fun HomeScreen(navController: NavHostController) {
    val viewModel = hiltViewModel<HomeViewModel>()
    var showDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showMenu by remember { mutableStateOf(false) }
    var activeFilter by remember { mutableStateOf("Todos") }
    val menuItems = listOf("Todos", "Apenas Favoritos", "Desc. por data", "Asc. por data")

    Scaffold(topBar = {
        TopAppBar(title = { Text("Supamail") }, actions = {
            IconButton(onClick = { showMenu = true }) {
                Icon(Icons.Filled.Menu, contentDescription = "Filtrar")
            }
            DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                DropdownMenuItem(text = { Text("Filtros") }, onClick = {})
                Divider()
                menuItems.forEach { label ->
                    DropdownMenuItem(text = {
                        Text(
                            label,
                            style = if (activeFilter == label) MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            ) else MaterialTheme.typography.bodySmall
                        )
                    }, onClick = {
                        showMenu = false
                        activeFilter = label
                        when (label) {
                            "Todos" -> {
                                viewModel.setFilter(HomeViewModel.Filter.None)
                            }

                            "Apenas Favoritos" -> {
                                viewModel.setFilter(HomeViewModel.Filter.Favorite)
                            }

                            "Desc. por data" -> {
                                viewModel.setFilter(HomeViewModel.Filter.DescDate)
                            }

                            "Asc. por data" -> {
                                viewModel.setFilter(HomeViewModel.Filter.AscDate)
                            }
                        }
                    })
                }
            }
        })
    }, floatingActionButton = {
        FloatingActionButton(onClick = { showDialog = true }) {
            Icon(Icons.Filled.Add, contentDescription = "Adicionar novo email")
        }
    }) { padding ->
        if (showDialog) {
            NewEmailDialog(homeViewModel = viewModel, onDialogClose = { showDialog = false })
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            SearchEmailBar(searchQuery = searchQuery, onSearchQueryChange = { newValue ->
                searchQuery = newValue
                viewModel.getEmailsBySubject(searchQuery)
            })
            Content(
                homeViewModel = viewModel,
                onEmailClick = { email -> navController.navigate("emailDetails/${email.id}") },
            )
        }
    }
}

@Composable
fun Content(
    homeViewModel: HomeViewModel, onEmailClick: (EmailEntity) -> Unit
) {
    val emails = homeViewModel.emails.collectAsState(emptyList())

    LaunchedEffect(key1 = true, block = {
        homeViewModel.setFilter(HomeViewModel.Filter.None)
    })

    LazyColumn(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (emails.value.isEmpty()) {
            item {
                Text(
                    text = "Nenhum email encontrado. \uD83D\uDE14",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 64.dp)
                )
            }
        } else {
            items(emails.value) { email ->
                EmailCard(email = email,
                    homeViewModel = homeViewModel,
                    modifier = Modifier.padding(vertical = 8.dp),
                    onEmailClick = {
                        onEmailClick(email)
                        homeViewModel.setEmailAsOpened(email.id)
                    })
            }
        }
    }
}