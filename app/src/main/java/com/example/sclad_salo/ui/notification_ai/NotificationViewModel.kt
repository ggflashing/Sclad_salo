package com.example.sclad_salo.ui.notification_ai

import android.view.translation.Translator
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.sclad_salo.models.UnitModel
import com.google.firebase.BuildConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


data class AiTipsUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val statusMessage: String? = null,
    val commercialOffer: String? = null,
    val targetAudience: String? = null,


)



@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle

): ViewModel() {
    private val _unitModel = MutableStateFlow<UnitModel?>(
        savedStateHandle.get<UnitModel>("key_product")
    )

    val unitModel = _unitModel.asStateFlow()

    fun setUnit(unit: UnitModel) {
        _unitModel.value = unit

    }

    private val _uiuState = MutableStateFlow(AiTipsUiState())

    val uiState = _uiuState.asStateFlow()
    private val geminiApiKey = BuildConfig.DEBUG


    private val geminiApiUrl: String
        get() = "Ключ гемини"

    private var translator: Translator? = null




}