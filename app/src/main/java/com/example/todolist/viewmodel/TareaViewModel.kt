package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.api.PhraseApiClient
import com.example.todolist.model.EstadoTarea
import com.example.todolist.model.Tarea
import com.example.todolist.model.TareaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

class TareaViewModel(private val repository: TareaRepository) : ViewModel() {

    private val phraseApiClient = PhraseApiClient()

    val tasks: StateFlow<List<Tarea>> = repository.tareas
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    private val _phrase = MutableStateFlow("Lista de tareas")
    val phrase: StateFlow<String> = _phrase.asStateFlow()

    init {
        fetchPhrase()
    }

    private fun fetchPhrase() {
        viewModelScope.launch {
            val newPhrase = phraseApiClient.getPhrase()
            if (!newPhrase.contains("Error")) {
                _phrase.value = newPhrase
            }
        }
    }

    fun addTask(titulo: String, descripcion: String, fecha: Date) {
        if (titulo.isNotBlank()) {
            viewModelScope.launch {
                val nuevaTarea = Tarea(titulo = titulo.trim(), descripcion = descripcion.trim(), fecha = fecha)
                repository.agregarTarea(nuevaTarea)
            }
        }
    }

    fun removeTask(tarea: Tarea) {
        viewModelScope.launch {
            repository.eliminarTarea(tarea)
        }
    }

    fun cambiarEstadoTarea(tarea: Tarea, estado: EstadoTarea) {
        viewModelScope.launch {
            val tareaActualizada = tarea.copy(estado = estado)
            repository.actualizarTarea(tareaActualizada)
        }
    }
}
