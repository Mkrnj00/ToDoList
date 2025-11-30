package com.example.todolist.data.api

import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class PhraseResponse(@SerializedName("text") val text: String?)

class PhraseApiClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.positive-api.online/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: PhraseApiService = retrofit.create(PhraseApiService::class.java)

    suspend fun getPhrase(): String {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getPhrase()
                response.text ?: "La API no devolvió una frase."
            } catch (e: Exception) {
                "Error al obtener la frase."
            }
        }
    }
}