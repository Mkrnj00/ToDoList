package com.example.todolist.data.api

import retrofit2.http.GET

interface PhraseApiService {
    @GET("phrase/esp")
    suspend fun getPhrase(): PhraseResponse
}