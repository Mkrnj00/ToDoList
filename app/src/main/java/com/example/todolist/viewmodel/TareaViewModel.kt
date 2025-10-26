package com.example.todolist.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.todolist.model.Tarea
import com.example.todolist.model.TareaRepository

class TareaViewModel : ViewModel() {
    private val _tasks = mutableStateListOf<Tarea>()
    val tasks: List<Tarea> = _tasks

    init {
        _tasks.addAll(TareaRepository.obtenerTareas())
    }

    fun addTask(titulo: String, descripcion: String) {
        if (titulo.isNotBlank()) {
            val nuevaTarea = Tarea(titulo.trim(), descripcion.trim())
            TareaRepository.agregarTarea(nuevaTarea)
            _tasks.add(nuevaTarea)
        }
    }

    fun removeTask(tarea: Tarea) {
        TareaRepository.eliminarTarea(tarea)
        _tasks.remove(tarea)
    }

    fun cambiarEstadoTarea(tarea: Tarea, completada: Boolean) {
        val tareaActualizada = tarea.copy(completada = completada)
        TareaRepository.actualizarTarea(tareaActualizada)
        val index = _tasks.indexOf(tarea)
        if (index != -1) {
            _tasks[index] = tareaActualizada
        }
    }
}
