package com.example.sclad_salo.sign_in

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sclad_salo.repository.OperatorsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//Закрытый класс для одноразовых событый от ViewModel к пользовательскому интерфейсу


sealed class LoginResult{

    data object Success: LoginResult()

    data class Error(val message: String): LoginResult()

}

//Класс данных для хранения всего состояния экрана входа


data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val operatorsRepository: OperatorsRepository
): ViewModel() {

    //Единый источник для состояния пользовательского интерфейса:

    var uiState by mutableStateOf(LoginState())
    private set//Пользовательский интерфейс может только читать это состояние

    //Поток для отправки одноразовых событий

    private val _loginResult = MutableSharedFlow<LoginResult>()

    val loginResult = _loginResult.asSharedFlow()

    //Функции пользовательского интерфейса для Логин-входа

    fun onEmailChange(newEmail: String) {
        uiState = uiState.copy(email = newEmail)

    }


    fun onPasswordChange(newPassword: String){
        uiState = uiState.copy(password = newPassword)

    }


    //Функция для кнопки логин входа

    fun onLoginClicked() {
        if (uiState.email.isBlank() || uiState.password.isBlank()) {
            viewModelScope.launch {
                _loginResult.emit(LoginResult.Error("Email and password cannot be empty."))



            }
            return

        }

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                //Делегируем логику входа в репозиторий
                operatorsRepository.singOperator(uiState.email,uiState.password)
                _loginResult.emit(LoginResult.Success)

            }catch (e: Exception) {
                _loginResult.emit(LoginResult.Error(e.message ?: "an uknown error occerred"))


            }finally {
                uiState = uiState.copy(isLoading = false)

            }


        }


    }



}