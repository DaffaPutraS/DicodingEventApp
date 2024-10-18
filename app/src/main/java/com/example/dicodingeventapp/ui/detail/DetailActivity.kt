package com.example.dicodingeventapp.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dicodingeventapp.R
import com.example.dicodingeventapp.data.repository.EventRepository
import com.example.dicodingeventapp.data.response.ListEventsItem
import com.example.dicodingeventapp.data.retrofit.ApiClient
import com.example.dicodingeventapp.databinding.ActivityDetailBinding
import org.jsoup.Jsoup

class DetailActivity : AppCompatActivity() {
    private lateinit var detailViewModel: DetailEventViewModel
    private lateinit var binding: ActivityDetailBinding
    private lateinit var repository: EventRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = EventRepository(ApiClient.apiService)

        detailViewModel = ViewModelProvider(this)[DetailEventViewModel::class.java]
        val eventId = intent.getIntExtra("EVENT_ID", -1)
        if (eventId != -1) {
            detailViewModel.fetchEventDetail(eventId)
            showLoading(true) // Menampilkan progress bar saat memuat data
        }

        detailViewModel.eventDetail.observe(this, Observer { event ->
            event?.let {
                bindEventDetail(it)
                showLoading(false) // Menyembunyikan progress bar setelah data dimuat
            }
        })

        detailViewModel.loading.observe(this, Observer { isLoading ->
            showLoading(isLoading) // Mengamati status loading dari ViewModel
        })
    }

    @SuppressLint("SetTextI18n")
    private fun bindEventDetail(it: ListEventsItem) {
        val htmlContent = it.description
        val document = Jsoup.parse(htmlContent)
        document.select("img").remove()
        val cleanedHtml = document.html()
        val linkItem = it.link

        binding.tvNameEvent.text = it.name
        binding.tvOwnerName.text = it.ownerName
        binding.tvBeginTime.text = it.beginTime
        binding.tvDescriptionEvent.text = Html.fromHtml(cleanedHtml, Html.FROM_HTML_MODE_LEGACY)
        binding.tvQuotaResgistrant.text = (it.quota - it.registrants).toString()
        Glide.with(this)
            .load(it.mediaCover)
            .into(binding.ivImageEvent)

        binding.btnRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(linkItem)
            }
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        binding.scrollView2.visibility = if (isLoading) android.view.View.GONE else android.view.View.VISIBLE
    }
}