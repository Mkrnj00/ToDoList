package com.example.todolist.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.todolist.data.core.saveImageToInternalStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarTareaView(
    onTaskAdded: (String, String, Date, String?) -> Unit,
    onBack: () -> Unit
) {
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }
    val showDatePicker = remember { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf(Date()) }
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let { 
                imageUri = saveImageToInternalStorage(context, it)
            }
        }
    )


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
                    taskTitle = it.uppercase()
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
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { showDatePicker.value = true }) {
                Text(text = "Seleccionar Fecha: ${sdf.format(selectedDate.value)}")
            }

            if (showDatePicker.value) {
                val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedDate.value.time)
                DatePickerDialog(
                    onDismissRequest = { showDatePicker.value = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let {
                                selectedDate.value = Date(it)
                            }
                            showDatePicker.value = false
                        }) {
                            Text("Aceptar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker.value = false }) {
                            Text("Cancelar")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { galleryLauncher.launch("image/*") }) {
                Text("Seleccionar imagen")
            }

            imageUri?.let {
                Spacer(modifier = Modifier.height(16.dp))
                AsyncImage(
                    model = it,
                    contentDescription = "Imagen de la tarea",
                    modifier = Modifier.size(150.dp),
                    contentScale = ContentScale.Crop
                )
            }


            if (errorText != null) {
                Text(text = errorText!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                if (taskTitle.isBlank()) {
                    errorText = "El campo de título no puede estar vacío"
                } else {
                    onTaskAdded(taskTitle.trim(), taskDescription.trim(), selectedDate.value, imageUri?.toString())
                }
            }) {
                Text("Guardar tarea")
            }
        }
    }
}
