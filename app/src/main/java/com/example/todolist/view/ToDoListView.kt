package com.example.todolist.view


import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.todolist.model.EstadoTarea
import com.example.todolist.model.Tarea
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoListView(
    tasks: List<Tarea>,
    phrase: String,
    onAddClicked: () -> Unit,
    onDelete: (Tarea) -> Unit,
    onTaskStateChange: (Tarea, EstadoTarea) -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf<Tarea?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(phrase) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClicked) {
                Icon(Icons.Default.Add, contentDescription = "Agregar tarea")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (tasks.isEmpty()) {
                Text(
                    text = "No hay tareas aún.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn {
                    items(tasks, key = { it.id }) { task ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            var expanded by remember { mutableStateOf(false) }

                            Box {
                                Text(
                                    text = task.estado.displayName,
                                    modifier = Modifier.clickable { expanded = true }
                                )
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    EstadoTarea.values().forEach { estado ->
                                        DropdownMenuItem(
                                            text = { Text(estado.displayName) },
                                            onClick = {
                                                onTaskStateChange(task, estado)
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = task.titulo, style = MaterialTheme.typography.bodyLarge)
                                Text(text = task.descripcion)
                                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                Text(text = sdf.format(task.fecha))
                            }
                            IconButton(onClick = {
                                showDialog = task
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Eliminar tarea",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                        Divider()
                    }
                }
            }
        }
    }

    showDialog?.let { task ->
        AlertDialog(
            onDismissRequest = { showDialog = null },
            title = { Text("Eliminar Tarea") },
            text = { Text("¿Estas seguro que quieres eliminar la tarea \"${task.titulo}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(task)
                    showDialog = null
                    Toast.makeText(context, "Tarea eliminada", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
