package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolist.ui.theme.ToDoListTheme
import com.example.todolist.view.AgregarTareaView
import com.example.todolist.view.TodoListView
import com.example.todolist.viewmodel.TareaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoListTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    val viewModel: TareaViewModel = viewModel()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            TodoListView(
                tasks = viewModel.tasks,
                onAddClicked = { navController.navigate("add") },
                onDelete = { task -> viewModel.removeTask(task) },
                onTaskStateChange = { task, isChecked -> viewModel.cambiarEstadoTarea(task, isChecked) }
            )
        }
        composable("add") {
            AgregarTareaView(
                onTaskAdded = { titulo, descripcion -> viewModel.addTask(titulo, descripcion) },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
