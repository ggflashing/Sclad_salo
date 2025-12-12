package com.example.sclad_salo.ui.Dashboard

import android.net.Uri

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.sclad_salo.R

@Composable
fun DashboardPage_unit_create(

    onNavigateBack: () -> Unit,
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel()

) {

    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUri ?: R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .padding(bottom = 16.dp)

        )

        Button(onClick = {
            launcher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )

        }) {

            Text("Pick image")

        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = unitName,
            onValueChange = { unitName = it },
            label = { Text("Unit name") },
            modifier = Modifier.fillMaxWidth()


        )
    }
    Spacer(modifier = Modifier.height(12.dp))
    OutlinedTextField(
        value = unitPrice,
        onValueChange = { unitPrice = it },
        label = { Text("Unit price") },
        modifier = Modifier.fillMaxWidth()

    )

    Spacer(modifier = Modifier.height(12.dp))
    OutlinedTextField(
        value = unitCount,
        onValueChange = { unitCount = it },
        label = { Text("Unit Count") },
        modifier = Modifier.fillMaxWidth()


    )
    Spacer(modifier = Modifier.height(12.dp))
    OutlinedTextField(
        value = unitComment,
        onValueChange = { unitComment = it },
        label = { Text("Unit comment") },
        modifier = Modifier.fillMaxWidth()


    )
    Spacer(modifier = Modifier.height(16.dp))

    if (isUploading){
        CircularProgressIndicator(progress = unitProgress)

    }else {
        Button(
            onClick = {
                imageUri?.let {
                    viewModel.addNewUnit(
                        name = unitName,
                        price = unitPrice.toDoubleOrNull() ?: 0.0,
                        count = unitCount.toIntOrNull() ?: 0,
                        comment = unitComment,
                        imageUri = it
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Add New Unit")

        }

    }



}



