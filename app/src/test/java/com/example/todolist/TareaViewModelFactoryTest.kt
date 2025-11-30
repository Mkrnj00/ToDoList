package com.example.todolist

import com.example.todolist.model.TareaRepository
import com.example.todolist.viewmodel.TareaViewModel
import com.example.todolist.viewmodel.TareaViewModelFactory
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Test

class TareaViewModelFactoryTest {

    @Test
    fun `create should return a TareaViewModel instance`() {
        // 1. Arrange: Preparamos el escenario
        val repository = mockk<TareaRepository>() // Creamos un doble de TareaRepository
        val factory = TareaViewModelFactory(repository) // Creamos la fábrica a probar

        // 2. Act: Ejecutamos la acción que queremos probar
        val viewModel = factory.create(TareaViewModel::class.java)

        // 3. Assert: Verificamos que el resultado es el esperado
        assertTrue("El ViewModel debería ser una instancia de TareaViewModel", viewModel is TareaViewModel)
    }
}
