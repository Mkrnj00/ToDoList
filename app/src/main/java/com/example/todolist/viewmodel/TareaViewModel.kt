package com.example.todolist.viewmodel

import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.api.PhraseApiClient
import com.example.todolist.model.EstadoTarea
import com.example.todolist.model.Tarea
import com.example.todolist.model.TareaRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import java.util.TimeZone

class TareaViewModel(private val repository: TareaRepository) : ViewModel() {

    private val phraseApiClient = PhraseApiClient()

    val tasks: StateFlow<List<Tarea>> = repository.tareas
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    private val _phrase = MutableStateFlow("Lista de tareas")
    val phrase: StateFlow<String> = _phrase.asStateFlow()

    private val _navigateBack = MutableSharedFlow<Unit>()
    val navigateBack = _navigateBack.asSharedFlow()

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

    fun addTask(
        context: Context,
        titulo: String,
        descripcion: String,
        fecha: Date,
        imageUri: String? = null
    ) {
        viewModelScope.launch {
            if (titulo.isNotBlank()) {
                val nuevaTarea = Tarea(
                    titulo = titulo.trim(),
                    descripcion = descripcion.trim(),
                    fecha = fecha,
                    imageUri = imageUri
                )
                repository.agregarTarea(nuevaTarea)
                agregarEventoCalendario(context, titulo.trim(), fecha)
                _navigateBack.emit(Unit)
            }
        }
    }

    private fun agregarEventoCalendario(context: Context, titulo: String, fecha: Date) {
        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, fecha.time)
            put(CalendarContract.Events.DTEND, fecha.time)
            put(CalendarContract.Events.TITLE, titulo)
            put(CalendarContract.Events.CALENDAR_ID, 1)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }
        context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
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
