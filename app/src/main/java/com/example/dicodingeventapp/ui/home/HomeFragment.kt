package com.example.dicodingeventapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ViewFlipper
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.dicodingeventapp.R
import com.example.dicodingeventapp.adapter.EventAdapter
import com.example.dicodingeventapp.data.response.ListEventsItem
import com.example.dicodingeventapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val eventViewModel: HomeViewModel by viewModels()
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeFinishedEvents()
        observeActiveEvents()
        observeLoading()
        observeError()

        // Load active events for carousel
        eventViewModel.getActiveEvents()

        // Load finished events for RecyclerView
        eventViewModel.getHomeEvents()
    }

    private fun setupRecyclerView() {
        binding.rvFinished.layoutManager = LinearLayoutManager(context)
        binding.rvFinished.setHasFixedSize(true)
        eventAdapter = EventAdapter()
        binding.rvFinished.adapter = eventAdapter
    }

    private fun observeFinishedEvents() {
        eventViewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            eventAdapter.submitList(events)
        }
    }

    private fun observeActiveEvents() {
        eventViewModel.activeEvents.observe(viewLifecycleOwner) { events ->
            setupViewFlipper(events, binding.ViewFlipper)
        }
    }

    private fun observeLoading() {
        eventViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    // Error handling, similar to your friend's implementation
    private fun observeError() {
        eventViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                setupErrorLayout(errorMessage)
            } else {
                binding.ErrorLayout.root.visibility = View.GONE
            }
        }
    }

    private fun setupErrorLayout(error: String?) {
        // Tampilkan ErrorLayout
        binding.ErrorLayout.root.visibility = View.VISIBLE
        binding.ErrorLayout.tvError.text = getString(R.string.no_connection)

        binding.ErrorLayout.btnConnection.setOnClickListener {
            // Action saat tombol diklik
            binding.ErrorLayout.root.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

            // Panggil ulang event
            eventViewModel.getActiveEvents()
            eventViewModel.getHomeEvents()
        }
    }

    private fun setupViewFlipper(events: List<ListEventsItem>, viewFlipper: ViewFlipper) {
        viewFlipper.removeAllViews()
        for (event in events) {
            val view = layoutInflater.inflate(R.layout.item_corousel, null)
            val imageView = view.findViewById<ImageView>(R.id.iv_carousel)
            Glide.with(this)
                .load(event.mediaCover)
                .into(imageView)
            viewFlipper.addView(view)
        }
        viewFlipper.startFlipping()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
