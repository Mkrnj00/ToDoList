package com.example.todolist.navegation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.todolist.screens.AddTaskScreen
import com.example.todolist.screens.TodoListScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    val taskList = remember { mutableStateListOf<String>() }

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            TodoListScreen(
                tasks = taskList,
                onAddClicked = { navController.navigate("add") },
                onDelete = { task -> taskList.remove(task) }
            )
        }
        composable("add") {
            AddTaskScreen(
                onTaskAdded = { task -> taskList.add(task) },
                onBack = { navController.popBackStack() }
            )
        }
    }
}