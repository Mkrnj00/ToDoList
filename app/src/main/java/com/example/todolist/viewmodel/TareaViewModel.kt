package com.example.todolist.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.model.Tarea
import com.example.todolist.model.TareaRepository
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.Date

// Data class corrected to match the actual API response.
data class PhraseResponse(@SerializedName("text") val text: String?)

class TareaViewModel(private val repository: TareaRepository) : ViewModel() {

    val tasks: StateFlow<List<Tarea>> = repository.tareas
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    private val _phrase = MutableStateFlow("")
    val phrase: StateFlow<String> = _phrase.asStateFlow()

    // State to control dialog visibility
    private val _showPhraseDialog = MutableStateFlow(false)
    val showPhraseDialog: StateFlow<Boolean> = _showPhraseDialog.asStateFlow()

    init {
        fetchPhrase()
    }

    fun onDialogDismiss() {
        _showPhraseDialog.value = false
    }

    private fun fetchPhrase() {
        viewModelScope.launch {
            try {
                val newPhrase = withContext(Dispatchers.IO) {
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
                        "Error al cargar la frase (código: ${response.code})"
                    }
                }
                _phrase.value = newPhrase
                // Show the dialog only if the fetch was successful
                if (newPhrase.isNotBlank() && !newPhrase.contains("Error", ignoreCase = true) && !newPhrase.contains("inesperada", ignoreCase = true)) {
                    _showPhraseDialog.value = true
                }
            } catch (e: Exception) {
                Log.e("TareaViewModel", "Ocurrió un error inesperado al obtener la frase", e)
                _phrase.value = "Ocurrió un error inesperado."
            }
        }
    }

    fun addTask(titulo: String, descripcion: String, fecha: Date) {
        if (titulo.isNotBlank()) {
            viewModelScope.launch {
                val nuevaTarea = Tarea(titulo.trim(), descripcion.trim(), fecha = fecha)
                repository.agregarTarea(nuevaTarea)
            }
        }
    }

    fun removeTask(tarea: Tarea) {
        viewModelScope.launch {
            repository.eliminarTarea(tarea)
        }
    }

    fun cambiarEstadoTarea(tarea: Tarea, completada: Boolean) {
        viewModelScope.launch {
            val tareaActualizada = tarea.copy(completada = completada)
            repository.actualizarTarea(tareaActualizada)
        }
    }
}
