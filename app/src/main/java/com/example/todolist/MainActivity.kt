package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
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

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            ToDoListView(
                tasks = tasks,
                onAddClicked = { navController.navigate("add") },
                onDelete = { task -> viewModel.removeTask(task) },
                onTaskStateChange = { task, isChecked -> viewModel.cambiarEstadoTarea(task, isChecked) }
            )
        }
        composable("add") {
            AgregarTareaView(
                onTaskAdded = { titulo, descripcion, fecha -> viewModel.addTask(titulo, descripcion, fecha) },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
