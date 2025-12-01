package com.example.sclad_salo.ui.Dashboard

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sclad_salo.models.UnitModel
import com.example.sclad_salo.repository.OperatorsRepository
import com.example.sclad_salo.repository.UnitRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(

    //1. Inject repositories and dependencies
    private val unitRepository: UnitRepository,
    private val operatorsRepository: OperatorsRepository,
    private val firebaseAuth: FirebaseAuth

): ViewModel() {

    //Никаких изменений в потоках состояний - они уже четко определенны до открытия страницы!

    private val _uploadProgress = MutableStateFlow(0f)

    val uploadProgress: StateFlow<Float> = _uploadProgress.asStateFlow()




    private val _isUploading = MutableStateFlow(false)

    val isUploading : StateFlow<Boolean> = _isUploading

    //Fix add the declaration for _uploadStatus

    private val _uploadStatus = MutableStateFlow<String?>(null)
    val uploadStatus: StateFlow<String?> = _uploadStatus.asStateFlow()


     fun addNewUnit(
        name: String,
        price: Double,
        count: Int,
        comment: String,
        imageUri: Uri


    ){

        viewModelScope.launch {

            val user = firebaseAuth.currentUser
            if (user == null){
                Log.e("DashboardViewModel","User is not authenticated")
                _uploadStatus.value = "Error:User not logged in"
                return@launch


            }

        }


        _isUploading.value = true
        _uploadStatus.value = "Uploading image..."

        try {
            //2.Делегируем логику в репозиторий для загрузки изображений
            val imageUrl = unitRepository.uploadUnitImage(imageUri,name)
            _uploadStatus.value = "Image uploaded.Saving data.."

            val unitModel = UnitModel(

                name = name,
                price = price,
                count = count,
                comment = comment,
                image = imageUrl
            )

           unitRepository.addNewUnit(unitModel)
            _uploadStatus.value = "Unit saved. Updating operator stats..."

            operatorsRepository.incrementOperatorAddedProductCount(user.id)
            _uploadStatus.value = "Successfully added new unit!"




        }catch (e: Exception) {
            //обрабатываем ошибку
            Log.e("DashboardViewModel","Failed to add new unit",e)
            _uploadStatus.value = "Error: ${e.message}"


        }finally {
            //снова делаем переменную isUploading false для нового состояния
            _isUploading.value = false

        }


    }


}