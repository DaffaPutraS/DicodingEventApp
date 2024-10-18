package com.example.dicodingeventapp.data.retrofit

import com.example.dicodingeventapp.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(@Query("active") active: Int, @Query("limit") limit: Int? = null, @Query("q") keyword: String? = null): Call<EventResponse>

    @GET("events/{id}")
    fun getEventById(@Path("id") id: Int): Call<EventResponse>
}