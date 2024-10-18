package com.example.dicodingeventapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingeventapp.data.repository.EventRepository
import com.example.dicodingeventapp.data.response.EventResponse
import com.example.dicodingeventapp.data.response.ListEventsItem
import com.example.dicodingeventapp.data.retrofit.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventViewModel : ViewModel() {
    private val repository = EventRepository(ApiClient.apiService)
    private val _eventDetail = MutableLiveData<ListEventsItem>()
    val eventDetail: LiveData<ListEventsItem> get() = _eventDetail

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun fetchEventDetail(id: Int) {
        _loading.value = true // Menandai mulai loading
        repository.getEventById(id).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _loading.value = false // Menandai loading selesai
                if (response.isSuccessful) {
                    response.body()?.let { eventResponse ->
                        _eventDetail.value = eventResponse.event
                    }
                } else {
                    // Tangani kesalahan jika perlu
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _loading.value = false // Menandai loading selesai pada gagal
                // Tangani kesalahan jika perlu
            }
        })
    }
}