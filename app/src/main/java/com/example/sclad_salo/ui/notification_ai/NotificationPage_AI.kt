package com.example.sclad_salo.ui.notification_ai

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.sclad_salo.models.UnitModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationPage_AI(

    navController : NavController,
    onNavigateBack: () -> Unit,
    viewModel: NotificationViewModel = hiltViewModel()


){
    //Извлекаем данные из ПРЕДЫДУЩЕЙ записи в стеке возврата из savedStateHandle
    val selectedUnit = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<UnitModel>("key_product")

    //Передаем его в ViewModel
    LaunchedEffect(selectedUnit) {
        if (selectedUnit != null){
            viewModel.setUnit(selectedUnit)

        }

    }

    //Получаем состояние пользовательского интерфейса из ViewModel
    val uiState by viewModel.uiState.collectAsState()
    val unitState = viewModel.unitModel.collectAsState()
    val unit = unitState.value

    //Получим исходные данные Unit
    // Проверка на null обьекта unit

    if (unit == null) {
        //Show a loading spinner or error we wait for data
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            if (selectedUnit == null){
                Text("Error:Data lost during navigation.")
                Button(onClick = onNavigateBack) {
                    Text("Go Back")

                }

            }else{
                CircularProgressIndicator()

            }

        }

        return // Остановим дальнейшее выполнение здесь
        //Чтобы не пытаться получить доступ к пустым свойствам unit


    }
    Scaffold(
        topBar = {
            TopAppBar(title = {Text(text = unit!!.name)})
     }


    ){paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())//Colum буде скроллится

        ){
            //Отображаение начальной информации о продукте

            Card (modifier = Modifier.fillMaxWidth()) {
                Column (modifier = Modifier.padding(16.dp)) {
                    AsyncImage(
                        model = unit!!.image,
                        contentDescription = unit!!.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop

                    )

                    Spacer(modifier = Modifier.height(18.dp))
                    Text(text = "Price: ${unit!!.price}$", style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold, color = Color.Red
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = unit!!.comment, style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold)

                }

            }

            Spacer(modifier = Modifier.height(24.dp))

            //Кнопка для вызова ИИ

            Button(
                onClick = {viewModel.getAiRecommendations()},
                enabled = !uiState.isLoading, //Disable when loading
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ask AI for Marketing Tips")

            }

            Spacer(modifier = Modifier.height(16.dp))


            //Отображаение индикаторов загрузки и результатов

            if (uiState.isLoading){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    uiState.statusMessage?.let {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = it)

                    }

                }


            }
            uiState.errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)

            }

            //Отображаение переведенных результатов ИИ

            uiState.commercialOffer?.let {
                AiResultSelection("Commercial offer", it)

            }
            uiState.commercialOffer?.let {
                AiResultSelection("Target Audience", it)

            }
            uiState.commercialOffer?.let {
                AiResultSelection("Example Businesses", it)

            }


        }



    }



}

@Composable
fun AiResultSelection(title: String,content: String){
    Column(modifier = M) {  }

}