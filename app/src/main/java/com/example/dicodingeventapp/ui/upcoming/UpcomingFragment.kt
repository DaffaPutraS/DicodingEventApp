package com.example.dicodingeventapp.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingeventapp.R
import com.example.dicodingeventapp.adapter.EventAdapter
import com.example.dicodingeventapp.databinding.FragmentUpcomingBinding

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private val eventViewModel: UpcomingViewModel by viewModels()
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeEvents()
        observeLoading()
        observeError()

        // Load upcoming events
        eventViewModel.getUpcomingEvents()
    }

    private fun setupRecyclerView() {
        binding.rvUpcoming.layoutManager = LinearLayoutManager(context)
        binding.rvUpcoming.setHasFixedSize(true)
        eventAdapter = EventAdapter()
        binding.rvUpcoming.adapter = eventAdapter
    }

    private fun observeEvents() {
        eventViewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            eventAdapter.submitList(events)
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
        binding.ErrorLayout.root.visibility = View.VISIBLE
        binding.ErrorLayout.tvError.text = getString(R.string.no_connection)

        binding.ErrorLayout.btnConnection.setOnClickListener {
            binding.ErrorLayout.root.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

            eventViewModel.getUpcomingEvents()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}