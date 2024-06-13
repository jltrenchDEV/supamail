package com.fiap.supamail.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fiap.supamail.presentation.HomeScreen
import com.fiap.supamail.presentation.HomeViewModel
import com.fiap.supamail.presentation.components.EmailDetails

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("emailDetails/{emailId}") { backStackEntry ->
            val emailIdStr = backStackEntry.arguments?.getString("emailId")
            val emailId = emailIdStr?.toIntOrNull()
            val viewModel: HomeViewModel = hiltViewModel()
            val email by viewModel.email.collectAsState(initial = null)

            LaunchedEffect(emailId) {
                viewModel.getEmailById(emailId)
            }

            val snackbarHostState = remember { SnackbarHostState() }

            email?.let {
                EmailDetails(
                    viewModel,
                    navigateBack = { navController.popBackStack() })
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                SnackbarHost(snackbarHostState)
                LaunchedEffect(key1 = true) {
                    snackbarHostState.showSnackbar("Email não encontrado")
                }
            }
        }
    }
}