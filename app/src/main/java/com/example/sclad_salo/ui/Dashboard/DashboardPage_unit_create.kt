package com.example.sclad_salo.ui.Dashboard

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun DashboardPage_unit_create (

    onNavigateBack: () -> Unit,
    navController : NavController,
    viewModel: DashboardViewModel = hiltViewModel()

){

    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia
    ) {
        uri:Uri? ->
        imageUri = uri

    }

    var unitName by remember { mutableStateOf("") }
    var unitPrice by remember { mutableStateOf("") }
    var unitCount by remember { mutableStateOf("") }
    var unitComment by remember { mutableStateOf("") }
    val unitProgress by viewModel.uploadProgress.collectAsState()
    val isUploading by viewModel.isUploading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        horizontolAligment = Aligment.Cent

    ) {
        Image()
    }



}