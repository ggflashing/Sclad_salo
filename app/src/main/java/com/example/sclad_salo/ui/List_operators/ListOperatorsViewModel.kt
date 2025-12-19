package com.example.sclad_salo.ui.List_operators

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sclad_salo.models.Sclad_operator
import com.example.sclad_salo.repository.OperatorsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ListOperatorsUiState(
    val operators: List<Sclad_operator> = emptyList(),
    val isLoading: Boolean = true

)

@HiltViewModel
class ListOperatorsViewModel @Inject constructor(
    // Hilt автоматически внедряет репозитоорий больше не нужно создавать его самостоятельно
    //Для получения списка операторов для действий с элементами списка
    private val operatorsRepository: OperatorsRepository

) : ViewModel(){
    //Предоставляет список операторов в виде StateFlow для сбора текущ инфы
    //Пользовательским интерфейсом Compose
    //Пользовательский интерфейс всегда будет отображать актуальный список операторов

    val uiState : StateFlow<ListOperatorsUiState> = operatorsRepository.getOperatorsList()
    //Оператор .stateIn преобразует холодный поток состояний из репозитория
    //В горячий поток состояния который может отслеживать пользовательский интерфейс
        .stateIn(
            scope = viewModelScope,
            //Поддерживает активность потока в течении 5 секунд после того
            //как пользовательский интерфейс перестанет прослушивать запросы
            //Это предоставращает повторный запрос к базе данных при простых изменениях конфигурации
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ListOperatorsUiState()

        ).let { flow ->
            //Это способ сопоставить необработанный список из репозитория с нашим классом UiState
            val newFlow = MutableStateFlow(ListOperatorsUiState())
            viewModelScope.launch {
                flow.collect{operatorsList ->
                    newFlow.value = ListOperatorsUiState(
                        operators = operatorsList as List<Sclad_operator>,
                        isLoading = false


                    )

                }
            }
            newFlow

        }

    //Удаляет оператора с помощью corutines
    //Это действие типа запустил и забыл запускаемое пользовательским интерфейсом

    fun deleteOperator(uid: String){
        //Запускам сопрограмму в области видимости
        //ViewModel для обработки асинхроннной операции
        viewModelScope.launch {
            try {
                Log.d("ListOperatorsViewModel", "Calling repo to delete UID: '$uid'")
                operatorsRepository.deleteOperator(uid)


            }catch (e: Exception){
                Log.e("ListOperatorsViewModel", "Failed to delete operator",e)
                //Здесь можно сгенерировать событие ошибки
                //Чтобы отобразить всплывающее сообщение (toast)


            }


        }

    }

    suspend fun getOperatorByUid(uid: String) {
        viewModelScope.launch {
            val operator = operatorsRepository.getOperatorByUid(uid)
            Log.d("ListOperatorsViewModel", "Operator: $operator")

        }


    }

    fun isOwner(): Boolean{
        if (operatorsRepository.getCurrentOperator_uid() == "Здесь ключь от firebase"){
            return true

        }else
            return false

    }





}