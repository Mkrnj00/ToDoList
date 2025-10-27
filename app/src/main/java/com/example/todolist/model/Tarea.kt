package com.example.todolist.model

import java.util.Date

data class Tarea(
    val titulo: String,
    val descripcion: String,
    val completada: Boolean = false,
    val fecha: Date = Date()
)
