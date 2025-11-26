package com.example.todolist.model

import com.example.todolist.data.TareasDataStore
import kotlinx.coroutines.flow.first

class TareaRepository(private val tareasDataStore: TareasDataStore) {

    val tareas = tareasDataStore.tareas

    suspend fun agregarTarea(tarea: Tarea) {
        val currentTasks = tareas.first().toMutableList()
        currentTasks.add(tarea)
        tareasDataStore.guardarTareas(currentTasks)
    }

    suspend fun eliminarTarea(tarea: Tarea) {
        val currentTasks = tareas.first().toMutableList()
        currentTasks.remove(tarea)
        tareasDataStore.guardarTareas(currentTasks)
    }

    suspend fun actualizarTarea(tareaActualizada: Tarea) {
        val currentTasks = tareas.first().toMutableList()
        val index = currentTasks.indexOfFirst { it.titulo == tareaActualizada.titulo }
        if (index != -1) {
            currentTasks[index] = tareaActualizada
            tareasDataStore.guardarTareas(currentTasks)
        }
    }
}
