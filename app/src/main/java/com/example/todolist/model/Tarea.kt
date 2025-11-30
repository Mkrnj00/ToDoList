package com.example.todolist.model

import java.util.Date
import java.util.UUID

data class Tarea(
    val id: String = UUID.randomUUID().toString(),
    val titulo: String,
    val descripcion: String,
    val estado: EstadoTarea = EstadoTarea.POR_HACER,
    val fecha: Date = Date(),
    val imageUri: String? = null
)
