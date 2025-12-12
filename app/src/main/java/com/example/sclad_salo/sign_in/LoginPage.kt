package com.example.sclad_salo.sign_in

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage ( viewModel: LoginViewModel = hiltViewModel(),
                onLoginSuccess: () -> Unit,
                onNavigateToRegistration: () -> Unit

) {
    //Получаем состояние пользовательского интерфейса непостредственно из ViewModel

    val uiState = viewModel.uiState
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loginResult.collect { result ->

            when(result) {
                is LoginResult.Success -> {

                    Toast.makeText(context,"Login successful", Toast.LENGTH_SHORT).show()
                    onLoginSuccess() //Активируем навигацию на главный экран

                }

                is LoginResult.Error -> {
                    Toast.makeText(context,result.message, Toast.LENGTH_SHORT).show()

                }


            }

        }


    }


    Scaffold (
        topBar = { TopAppBar(title = { Text("Login") })
        }

    ){ paddingValues ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center



        ){
            Text("Welcome Back", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))


            //Поле ввода email

            OutlinedTextField(

                value = uiState.email,
                onValueChange = viewModel::onEmailChange,//вызов ViewModel при изменении
                label = {Text("Email")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true

            )

            Spacer(modifier = Modifier.height(16.dp))

            //Поле ввода Password с сокрытием символов

            OutlinedTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,//Вызов функции ViewModel при изменении
                label = {Text("password")},
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true


            )
            Spacer(modifier = Modifier.height(32.dp))

            //Кнопка Login


            Button(
                onClick = viewModel::onLoginClicked,//Вызов функции ViewModel при изменении
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !uiState.isLoading//Отключаем кнопку во время загрузки


            ) {

                if (uiState.isLoading) {

                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary

                    )

                }else {
                    Text("Log In")

                }

            }
            Spacer(modifier = Modifier.height(16.dp))
            //Переходим к кнопке регистрации
            TextButton(onClick = onNavigateToRegistration) {
                Text("Dont have an account? Register Now")

            }

        }


    }



}