package com.example.sclad_salo.sign_up

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


//Этот запечатанный класс RegistrationResult представляет
//Все одноразовые события которые возможны
//И они используются классом ViewModel
//Для сигналов к изменению пользовательского интерфейса в зависимоти от событий

sealed class RegistrationResult{

    data object Success : RegistrationResult()

    data class Error(val message: String) : RegistrationResult()

}

//Этот класс данных хранит состояиние пользовательского интерфейса
//фиксирующее текущее состояние регистрации пользователя которое может меняться со временем

data class RegistrationUiState(

    val isLoading: Boolean = false,
    val name : String = "",
    val email : String = "",
    val password: String = "",
    val selectedRole: String = "OPERATOR"//Default role

)



@HiltViewModel
class RegistrationPageViewModel @Inject constructor(

    private val operatorsRepository: OperatorsRepository

): ViewModel() {
    //Сохраняет текущее состояние регистрации пользователя для интерфейса управляемое ViewModel

    var uiState by mutableStateOf(value = RegistrationUiState())
        private set // Пользовательский интерфейс может считывать это состояние но не может изменить

    // asSharedFlow() для отслеживания и отправки одноразовых событий
    //при регистрации например навигации или уведомлений в пользовательский интерфейс

    private val _registrationResult = MutableSharedFlow<RegistrationResult>()

    val registrationResult = _registrationResult.asSharedFlow()

    //Функции вызываемыке пользовательским интерфейсом для обновления состояния

    fun onNameChancge(newName: String) {
        uiState = uiState.copy(name = newName)

    }

    fun onEmailChange(newEmail: String) {
        uiState = uiState.copy(email = newEmail)

    }

    fun onPassword(newPassword: String) {
        uiState = uiState.copy(password = newPassword)

    }

    fun onRoleSelected(newRole: String) {
        uiState = uiState.copy(selectedRole = newRole)

    }

//Основная функция логики регистрации


    fun onRegisterClicked() {
        //Базовая проверка

        if (uiState.name.isBlank() || uiState.email.isBlank() || uiState.password.isBlank()) {
            viewModelScope.launch {
                _registrationResult.emit(RegistrationResult.Error("All fiewkd are required"))

            }
            return

        }

        viewModelScope.launch {
            //Устанавливаем состояние загрузки на true

            uiState = uiState.copy(isLoading = true)

            try {
                //Делегируем сложную логику регистрации в репозитторий
                operatorsRepository.createNewOperator(
                    name = uiState.name,
                    email = uiState.email,
                    password = uiState.password,
                    role = uiState.selectedRole

                )
                //Дадим событие успеха если не было создана исключение
                _registrationResult.emit(RegistrationResult.Success)
            }catch (e: Exception){
                //Выдаем событие ошибки с конктертным сообщением из исключения
                _registrationResult.emit(
                    RegistrationResult.Error(
                        e.message?:"An uknown error occerred"

                    )
                )


            }finally {
                //Устанавливаем состояние загрузки обратно на false даже если загрузка удалась
                uiState = uiState.copy(isLoading = false)
            }


        }

    }




}