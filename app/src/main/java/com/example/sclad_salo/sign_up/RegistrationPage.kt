package com.example.sclad_salo.sign_up

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationPage ( viewModel: RegistrationPageViewModel = hiltViewModel(),
                onRegistrationSuccess: () -> Unit


)// Обратный вызов для навигации в случае успеха
 {


    val uiState = viewModel.uiState

     val context = LocalContext.current

     LaunchedEffect (Unit){
         viewModel.registrationResult.collectLatest { result ->
             when(result) {
                 is RegistrationResult.Success -> {
                     Toast.makeText(context,"Registration successful!", Toast.LENGTH_SHORT).show()
                     onRegistrationSuccess()//Обратный вызов в случае успеха

                 }

                 is RegistrationResult.Error -> {
                     Toast.makeText(context,result.message, Toast.LENGTH_SHORT).show()


                 }


             }


         }

     }

     Scaffold (topBar = { TopAppBar(title = { Text("Register New User") }) }

     ){paddingValues ->
         Column (
             modifier = Modifier
                 .fillMaxSize()
                 .padding(paddingValues)
                 .padding(horizontal = 24.dp)
                 .verticalScroll(rememberScrollState()), // Сделаем вертикальный скролл страницы
             horizontalAlignment = Alignment.CenterHorizontally,
             verticalArrangement = Arrangement.Center
         ){
             Text("Create an Account", style = MaterialTheme.typography.headlineMedium)
             Spacer(modifier = Modifier.height(24.dp))

             //Поля ввода данных пользователя имя,почта,пароль
             OutlinedTextField(
                 value = uiState.name,
                 onValueChange = {viewModel.onNameChancge(it)},
                 label = {Text("Name and Surname")},
                 modifier = Modifier.fillMaxWidth(),
                 singleLine = true

             )
             Spacer(modifier = Modifier.height(16.dp))

             OutlinedTextField(
                 value = uiState.email,
                 onValueChange = {viewModel.onEmailChange(it)},
                 label = {Text("Password")},
                 modifier = Modifier.fillMaxWidth(),
                 keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                 singleLine = true

             )

             Spacer(modifier = Modifier.height(24.dp))

             //Выбор роли пользователя при регистрации

             Text("Select Role", style = MaterialTheme.typography.titleMedium)
             Row (
                 modifier = Modifier.fillMaxWidth(),
                 verticalAlignment = Alignment.CenterVertically
             ){
                 RadioButton(
                     selected = uiState.selectedRole == "OPERATOR",
                     onClick = {viewModel.onRoleSelected("OPERATOR")}


                 )

                 Text(
                     text = "Operator",
                     modifier = Modifier
                         .selectable(
                             selected = uiState.selectedRole == "OPERATOR",
                             onClick ={viewModel.onRoleSelected("OPERATOR")}

                         ).padding(end = 16.dp)

                 )
                 RadioButton(
                     selected = uiState.selectedRole == "MARKET MANAGER",
                     onClick = {viewModel.onRoleSelected("MARKET MANAGER")}

                 )

                 Text(
                     text = "Market Manager",
                     modifier = Modifier.selectable(
                         selected = uiState.selectedRole == "MARKET MANAGER",
                         onClick = {viewModel.onRoleSelected("MARKET MANAGER")}
                     )


                 )


             }
             Spacer(modifier = Modifier.height(32.dp))
             //Кнопка регистрации
             Button(
                 onClick = {viewModel.onRegisterClicked()},
                 modifier = Modifier
                     .fillMaxWidth()
                     .height(50.dp),
                 enabled = !uiState.isLoading //Disable button while loading


             ) {
                 if (uiState.isLoading) {
                     CircularProgressIndicator(
                         modifier = Modifier.size(24.dp),
                         color = MaterialTheme.colorScheme.onPrimary
                     )

                 }else{
                     Text("Register")
                 }


             }




         }


     }



}// обратный вызов