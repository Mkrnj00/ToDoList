package com.example.todolist.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.todolist.model.Tarea
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TareasDataStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("tareas")
        private val TAREAS_KEY = stringPreferencesKey("tareas_list")
    }

    private val gson = Gson()

    suspend fun guardarTareas(tareas: List<Tarea>) {
        val jsonTareas = gson.toJson(tareas)
        context.dataStore.edit { preferences ->
            preferences[TAREAS_KEY] = jsonTareas
        }
    }

    val tareas: Flow<List<Tarea>> = context.dataStore.data
        .map { preferences ->
            val jsonTareas = preferences[TAREAS_KEY] ?: "[]"
            val type = object : TypeToken<List<Tarea>>() {}.type
            gson.fromJson(jsonTareas, type)
        }
}
