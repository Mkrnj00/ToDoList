package com.example.todolist.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.todolist.model.Tarea
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("tareas")

class TareasDataStore(private val context: Context) {

    companion object {
        private val TAREAS_KEY = stringPreferencesKey("tareas_list")
    }

    private val gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, DateTypeAdapter())
        .create()

    suspend fun guardarTareas(tareas: List<Tarea>) {
        val jsonTareas = gson.toJson(tareas)
        context.dataStore.edit { preferences ->
            preferences[TAREAS_KEY] = jsonTareas
        }
    }

    val tareas: Flow<List<Tarea>> = context.dataStore.data
        .map { preferences ->
            try {
                val jsonTareas = preferences[TAREAS_KEY] ?: "[]"
                val type = object : TypeToken<List<Tarea>>() {}.type
                gson.fromJson(jsonTareas, type) ?: emptyList()
            } catch (e: Exception) {
                // Si hay un error al leer, devuelve una lista vacía para evitar que la app crashee
                e.printStackTrace()
                emptyList<Tarea>()
            }
        }
}

// Adaptador para que Gson guarde las fechas como números (timestamps)
class DateTypeAdapter : com.google.gson.TypeAdapter<Date>() {
    override fun write(out: com.google.gson.stream.JsonWriter?, value: Date?) {
        if (value == null) {
            out?.nullValue()
        } else {
            out?.value(value.time)
        }
    }

    override fun read(`in`: com.google.gson.stream.JsonReader?): Date? {
        return if (`in` != null) {
            if (`in`.peek() == com.google.gson.stream.JsonToken.NULL) {
                `in`.nextNull()
                null
            } else {
                Date(`in`.nextLong())
            }
        } else {
            null
        }
    }
}
