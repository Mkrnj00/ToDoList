package com.example.todolist.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    onTaskAdded: (String) -> Unit,
    onBack: () -> Unit
) {
    var taskText by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva tarea") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = taskText,
                onValueChange = {
                    taskText = it
                    errorText = null
                },
                label = { Text("Nombre de la tarea") },
                isError = errorText != null,
                modifier = Modifier.fillMaxWidth()
            )

            if (errorText != null) {
                Text(text = errorText!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                if (taskText.isBlank()) {
                    errorText = "El campo no puede estar vacío"
                } else {
                    onTaskAdded(taskText.trim())
                    onBack()
                }
            }) {
                Text("Guardar tarea")
            }
        }
    }
}


