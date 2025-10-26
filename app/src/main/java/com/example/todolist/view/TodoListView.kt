package com.example.todolist.view


import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import com.example.todolist.model.Tarea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListView(
    tasks: List<Tarea>,
    onAddClicked: () -> Unit,
    onDelete: (Tarea) -> Unit,
    onTaskStateChange: (Tarea, Boolean) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lista de tareas") })
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
                    items(tasks, key = { it.titulo }) { task ->
                        var visible by remember { mutableStateOf(true) }

                        AnimatedVisibility(visible = visible) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = task.completada,
                                    onCheckedChange = { isChecked ->
                                        onTaskStateChange(task, isChecked)
                                    }
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = task.titulo)
                                    Text(text = task.descripcion)
                                }
                                IconButton(onClick = {
                                    visible = false
                                    onDelete(task)
                                    Toast.makeText(context, "Tarea eliminada", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Eliminar tarea",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                        Divider()
                    }
                }
            }
        }
    }
}
