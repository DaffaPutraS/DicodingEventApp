package com.example.dicodingeventapp.data.repository

import com.example.dicodingeventapp.data.response.EventResponse
import com.example.dicodingeventapp.data.retrofit.ApiService
import retrofit2.Call

class EventRepository(private val apiService: ApiService) {
    fun getEventById(id: Int): Call<EventResponse> {
        return apiService.getEventById(id)
    }
}