package com.example.sclad_salo.ui.notification_ai

import android.util.Log
import android.util.Log.e

import androidx.compose.ui.semantics.Role

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sclad_salo.models.UnitModel
import com.example.sclad_salo.BuildConfig
import com.google.android.gms.common.server.response.FastJsonResponse
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject


data class AiTipsUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val statusMessage: String? = null,
    val commercialOffer: String? = null,
    val targetAudience: String? = null,
    val exampleWebsites: String? = null


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
    private val geminiApiKey = BuildConfig.GEMINI_API_KEY.trim()


    private val geminiApiUrl: String
        get() = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=$geminiApiKey"

    private var translator: Translator? = null

    init {
        val translatorOption = TranslatorOptions.Builder()
        .setSourceLanguage("en")
            .setTargetLanguage("ru")
            .build()
        translator = Translation.getClient(translatorOption)
    }

    fun getAiRecommendations() {

        if (geminiApiKey == "null" || geminiApiKey.isBlank()) {

            _uiuState.value = AiTipsUiState(
                errorMessage = "API Key is missing" +
                        "Please check local.properies, then Build -> Rebuild Project."
            )

            return

        }

        viewModelScope.launch {
            _uiuState.value = AiTipsUiState(
                isLoading = true,
                statusMessage = "Generating AI tips..."
            )

            try {
                val aiResponse = fetchFromGemini()
                translateAllParts(aiResponse)

            } catch (e: Exception) {
                Log.e("NotificationsViewModel", "Failed to get AI recommendations", e)
                _uiuState.value =
                    _uiuState.value.copy(isLoading = false, errorMessage = "Error: ${e.message}")

            }

        }

    }

    private suspend fun fetchFromGemini(): String = withContext(Dispatchers.IO) {
        val promtText = createPromtForGemin(unitModel.value)
        val url = URL(geminiApiUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        val jsonPayload = JSONObject().apply {
            val parts = JSONArray().put(JSONObject().put("text", promtText))
            val contents = JSONArray().put(JSONObject().put("parts", parts))
            put("contents", contents)

        }


        OutputStreamWriter(connection.outputStream).use {
            it.write(jsonPayload.toString())

        }

        val responseCode = connection.responseCode

        if (responseCode == HttpURLConnection.HTTP_OK) {
            val responseText = connection.inputStream.bufferedReader().use {
                it.readText()}
            return@withContext parseGeminiResponse(responseText)


        }else{

            val errorBody = connection.errorStream?.bufferedReader()?.use {
                it.readText()
            } ?: "No details"
            throw Exception("Gemini Api Error ($responseCode):"+
            "$errorBody Ensure GeminiApi is enabled and your region"+
            "country and is supported.")

        }


    }

    private suspend fun translateAllParts(aiText: String) {

        val offerKey = "Commercial Offer:"
        val audieceKey = "Target Audience"
        val websitesKey = "Example Businesses/Websites:"

        if (!aiText.contains(offerKey) || !aiText.contains(audieceKey)) {
            _uiuState.value = _uiuState.value.copy(
                isLoading = false,
                errorMessage = "AI response format mismatch Try again"
            )
            return
        }


            val offer = aiText.substringAfter(offerKey).substringBefore(audieceKey).trim()
            val audience = aiText.substringAfter(audieceKey).substringBefore(websitesKey).trim()
            val websites = aiText.substringAfter(websitesKey).trim()


        _uiuState.value = _uiuState.value.copy(statusMessage = "Downloading translation model...")
        val downloadConditions = DownloadConditions.Builder().requireWifi().build()
        translator?.downloadModelIfNeeded(downloadConditions)?.await()

        _uiuState.value = _uiuState.value.copy(statusMessage = "Translating...")
        val translatedOffer = translator?.translate(offer)?.await()
        val translatedAudience = translator?.translate(offer)?.await()
        val translatedWebsites = translator?.translate(offer)?.await()


        _uiuState.value = AiTipsUiState(
            isLoading = false,
            commercialOffer = translatedOffer,
            targetAudience = translatedAudience,
            exampleWebsites = translatedWebsites

        )


    }

    private fun createPromtForGemin (product: UnitModel?): String{
        return """"
        Role: Chief Market sales Manager
        Task: Provide wholesale marketing strategy for the product below
        
        Product: ${product?.name ?: "Uknown"}
        Price: ${product?.price ?: 0} $
        Details: ${product?.comment ?: "No description"}
        
        Response Structure (REQUIRED HEADINGS):
        Commercial Offer: [Write offer here]
        Target Audience: [Write audience here]
        Example Businesses/Websites: [Write example here]
        
        """.trimIndent()


    }

    private fun parseGeminiResponse(jsonResponse: String): String{
        return try {
            JSONObject(jsonResponse)
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")

        }catch (e: Exception){

            "Error parsing AI result"
        }

    }

    override fun onCleared() {
        super.onCleared()
        translator?.close()
    }


}