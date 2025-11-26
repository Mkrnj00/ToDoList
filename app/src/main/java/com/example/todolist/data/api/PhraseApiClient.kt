package com.example.todolist.data.api

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

data class PhraseResponse(@SerializedName("text") val text: String?)

class PhraseApiClient {
    suspend fun getPhrase(): String {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url("https://www.positive-api.online/phrase/esp")
                    .build()
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        val phraseObject = Gson().fromJson(responseBody, PhraseResponse::class.java)
                        phraseObject.text ?: "La API no devolvió una frase."
                    } else {
                        "Respuesta vacía de la API"
                    }
                } else {
                    "Error al cargar la frase."
                }
            } catch (e: Exception) {
                "Error al obtener la frase."
            }
        }
    }
}