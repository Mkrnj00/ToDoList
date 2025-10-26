package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todolist.ui.theme.ToDoListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListTheme {
                TodoListScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen() {
    val taskList = remember { mutableStateListOf<String>() }
    var currentTaskText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Lista de Tareas (Compose)") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = currentTaskText,
                    onValueChange = { currentTaskText = it },
                    label = { Text("Nueva tarea") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (currentTaskText.isNotBlank()) {
                            taskList.add(currentTaskText.trim())
                            currentTaskText = ""
                        }
                    },
                    modifier = Modifier.height(56.dp)
                ) {
                    Text("Agregar")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(items = taskList, key = { it }) { task ->
                    TodoItem(
                        task = task,
                        onDelete = { taskList.remove(task) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun TodoItem(task: String, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onDelete)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = task,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = "Eliminar tarea",
            tint = MaterialTheme.colorScheme.error
        )
    }
}
