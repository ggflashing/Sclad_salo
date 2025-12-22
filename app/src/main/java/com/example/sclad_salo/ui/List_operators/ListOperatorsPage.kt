package com.example.sclad_salo.ui.List_operators

import androidx.appcompat.app.AlertDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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

    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            onConfirm = {
                onDeleteClick()
                showDeleteDialog = false
            },
        onDismiss = {
            showDeleteDialog = false
        }

        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)

    ){
        Column (
            modifier = Modifier.padding(16.dp)



        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                //Operator Name and Role
                Column (
                    modifier = Modifier.weight(1f)

                ){
                    Text(
                        text = operator.name_surname,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = operator.role,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )


                }
                //иконку удаления работников-операторов делаем видимой
                //Только если текущий пользователь - хозяин склада
                AnimatedVisibility(visible = isOwner) {
                    IconButton(onClick = {showDeleteDialog = true}) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Operator",
                            tint = MaterialTheme.colorScheme.error

                        )

                    }

                }

            }
            Spacer(modifier = Modifier.height(16.dp))

            //Operator Stats
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround

            ) {
                StatItem("Operator Code", operator.operators_code.toString())
                StatItem("Added Product", operator.added_product.toString())
                StatItem("Promoted", operator.promoted_product.toString())


            }


        }

    }



}

@Composable
private fun StatItem(label: String,value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally


    ){
        Text(text = label, style = MaterialTheme.typography.bodySmall)
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )


    }

}
@Composable
private fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {Text("Вы уверены?")},
        text = {Text("В случае удаления данные операторов восстановлению не подлежат")},
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Да, удалить")

            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Нет не удалять")
            }

        }
    )

}