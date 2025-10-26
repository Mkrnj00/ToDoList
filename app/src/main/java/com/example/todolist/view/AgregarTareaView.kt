package com.example.todolist.view

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
fun AgregarTareaView(
    onTaskAdded: (String, String) -> Unit,
    onBack: () -> Unit
) {
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
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
                value = taskTitle,
                onValueChange = {
                    taskTitle = it
                    errorText = null
                },
                label = { Text("Título de la tarea") },
                isError = errorText != null,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                label = { Text("Descripción de la tarea") },
                modifier = Modifier.fillMaxWidth()
            )

            if (errorText != null) {
                Text(text = errorText!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                if (taskTitle.isBlank()) {
                    errorText = "El campo de título no puede estar vacío"
                } else {
                    onTaskAdded(taskTitle.trim(), taskDescription.trim())
                    onBack()
                }
            }) {
                Text("Guardar tarea")
            }
        }
    }
}
