package com.example.todolist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.model.Tarea
import com.example.todolist.model.TareaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

class TareaViewModel(private val repository: TareaRepository) : ViewModel() {

    val tasks: StateFlow<List<Tarea>> = repository.tareas
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

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
