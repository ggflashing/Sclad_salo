package com.example.sclad_salo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sclad_salo.models.Sclad_operator
import com.example.sclad_salo.models.UnitModel
import com.example.sclad_salo.repository.UnitRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject




@HiltViewModel
class HomeViewModel @Inject constructor(unitRepository: UnitRepository): ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val database = FirebaseDatabase.getInstance()

    private val _units = MutableStateFlow<List<UnitModel>>(emptyList())

    //Создадим список "units" как поток StateFlow для сбора Compose UI

    val units: StateFlow<List<UnitModel>> = unitRepository.getUnitsList()
        .stateIn(
            scope = viewModelScope, //Лист отслеживается как ViewModeol функция
            started = SharingStarted.WhileSubscribed(5000),
            //Сохраняет поток активным
            //В течении 5 секунд после закрытия пользовательского интерфейса
            initialValue = emptyList() // Инициализирует сначала пустой list

        )


    private val _operatorCode = MutableStateFlow<Int?>(null)

    val operatorCode: StateFlow<Int?> = _operatorCode

    val currentUser = auth.currentUser

    init {
        fetchUnits()
        fetchOperatorCode()

    }

    private fun fetchUnits() {

        val unitsRef = database.getReference("units")

       unitsRef.addValueEventListener(object : ValueEventListener{
           override fun onDataChange(snapshot: DataSnapshot) {
               val unitsList = mutableListOf<UnitModel>()
               for (unitSnapshot in snapshot.children){

                   val unit = unitSnapshot.getValue(UnitModel::class.java)
                   unit?.let {
                       unitsList.add(it)
                   }

                   _units.value = unitsList


               }



           }

           override fun onCancelled(error: DatabaseError) {
               //Handle error
           }

       })

    }

    private fun fetchOperatorCode() {
        currentUser?.let {
            val operatorRef = database.getReference("user_persons").child(it.uid)

            operatorRef.get().addOnSuccessListener {
                if (it.exists()) {
                    val operator = it.getValue(Sclad_operator::class.java)
                    _operatorCode.value = operator?.operators_code

                }
            }


        }

    }

    //Проверяет корректность предоставленноого PIN-кода

    fun isPinCodeValid(pinCode: String): Boolean{

        return pinCode == "3434"
    }

    fun signOut(){

        auth.signOut()
    }



}