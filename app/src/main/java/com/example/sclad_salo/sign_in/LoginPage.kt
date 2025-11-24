package com.example.sclad_salo.sign_in

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage ( viewModel: LoginViewModel = hiltViewModel(),
                onLoginSuccess: () -> Unit,
                onNavigateToRegistration: () -> Unit

)