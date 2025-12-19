package com.example.sclad_salo.ui.List_operators

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.sclad_salo.models.Sclad_operator
import java.security.acl.Owner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListOperatorsPage(
    viewModel: ListOperatorsViewModel = hiltViewModel(),

    navController : NavHostController,
    onNavigateBack: () -> Unit


){// 1 Собираем все состояние пользовательского
    //интерфейса в виде обьекта Compose State:

    val uiState by viewModel.uiState.collectAsState()
    val isOwner = viewModel.isOwner() // Get the owner status from the ViewModel

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("List of Operators") })

        }

    ) {paddingValues ->
        //Обработка состояние загрузки:
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center

            ){
                CircularProgressIndicator()



            }

        }else{
            //Отображаем весь список операторов
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.operators) {operator->
                    OperatorItem(
                        operator = operator,
                        isOwner = isOwner,
                        onDeleteClick = {
                            //Даем задание ViewModel удалить оператора
                            viewModel.deleteOperator(operator.uid)

                        }

                    )

                }


            }
        }


    }

}

@Composable
private fun OperatorItem(

    operator: Sclad_operator,
    isOwner: Boolean,
    onDeleteClick:() -> Unit
){
    //Состояние для управления видимостью диалогового окна подтверждения удаления
    var showDeleteDialog by remember { mutableStateOf(false) }



}