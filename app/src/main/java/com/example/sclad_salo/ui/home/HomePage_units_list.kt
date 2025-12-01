package com.example.sclad_salo.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sclad_salo.models.UnitModel

@Composable
fun HomePage_units_list(

    onNavigateToDashboardPage: () -> Unit,
    navController : NavController,
    onNavigateToNofitications: (UnitModel) -> Unit,

    onNavigateToLoginPage: () -> Unit,

    viewModel: HomeViewModel = hiltViewModel()

) {

    val context = LocalContext.current
    val operatorCode by viewModel.



}