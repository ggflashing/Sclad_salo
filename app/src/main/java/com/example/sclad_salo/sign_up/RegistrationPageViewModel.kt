package com.example.sclad_salo.sign_up

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.Role
import androidx.lifecycle.ViewModel
import com.example.sclad_salo.repository.OperatorsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    var uiState by mutableStateOf(value = RegistrationUiState)
        private set // Пользовательский интерфейс может считывать это состояние но не может изменить

    // asSharedFlow() для отслеживания и отправки одноразовых событий
    //при регистрации например навигации или уведомлений в пользовательский интерфейс

    private val _registrationResult = MutableSharedFlow<RegistrationResult>()

    val registrationResult = _registrationResult.asSharedFlow()

    //Функции вызываемыке пользовательским интерфейсом для обновления состояния

    fun onNameChancge(newName: String) {
        uiState = uiState.copy(name = newName)

    }



}