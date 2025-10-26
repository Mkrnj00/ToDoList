package com.example.todolist.model

data class Tarea(
    val titulo: String,
    val descripcion: String,
    val completada: Boolean = false
)
