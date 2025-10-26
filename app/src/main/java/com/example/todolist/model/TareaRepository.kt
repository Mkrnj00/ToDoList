package com.example.todolist.model

object TareaRepository {
    private val tareas = mutableListOf<Tarea>()

    fun obtenerTareas(): List<Tarea> = tareas

    fun agregarTarea(tarea: Tarea) {
        tareas.add(tarea)
    }

    fun eliminarTarea(tarea: Tarea) {
        tareas.remove(tarea)
    }

    fun actualizarTarea(tareaActualizada: Tarea) {
        val index = tareas.indexOfFirst { it.titulo == tareaActualizada.titulo } // Asumimos que el título es único
        if (index != -1) {
            tareas[index] = tareaActualizada
        }
    }
}
