package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolist.data.TareasDataStore
import com.example.todolist.model.TareaRepository
import com.example.todolist.ui.theme.ToDoListTheme
import com.example.todolist.view.AgregarTareaView
import com.example.todolist.view.ToDoListView
import com.example.todolist.viewmodel.TareaViewModel
import com.example.todolist.viewmodel.TareaViewModelFactory
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListTheme {
                val navController = rememberNavController()
                val dataStore = TareasDataStore(applicationContext)
                val repository = TareaRepository(dataStore)
                val factory = TareaViewModelFactory(repository)
                AppNavGraph(navController = navController, factory = factory)
            }
        }
    }
}

@Composable
fun AppNavGraph(navController: NavHostController, factory: TareaViewModelFactory) {
    val viewModel: TareaViewModel = viewModel(factory = factory)
    val tasks by viewModel.tasks.collectAsState()
    val phrase by viewModel.phrase.collectAsState()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            ToDoListView(
                tasks = tasks,
                phrase = phrase,
                onAddClicked = { navController.navigate("add") },
                onDelete = { task ->
                    viewModel.removeTask(task)
                },
                onTaskStateChange = { task, estado ->
                    viewModel.cambiarEstadoTarea(task, estado)
                }
            )
        }
        composable("add") {
            // 1. Con este LaunchedEffect, "escuchamos" la señal del ViewModel.
            LaunchedEffect(Unit) {
                viewModel.navigateBack.collectLatest { 
                    // 3. Solo cuando la señal llega, navegamos hacia atrás.
                    navController.popBackStack()
                }
            }

            AgregarTareaView(
                // 2. Ahora, onTaskAdded SOLO le pide al ViewModel que guarde la tarea.
                // Ya no se encarga de la navegación.
                onTaskAdded = { titulo, descripcion, fecha, imageUri ->
                    viewModel.addTask(titulo, descripcion, fecha, imageUri)
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
