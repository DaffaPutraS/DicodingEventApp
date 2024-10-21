package com.example.dicodingeventapp.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingeventapp.R
import com.example.dicodingeventapp.adapter.EventAdapter
import com.example.dicodingeventapp.databinding.FragmentFinishedBinding

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private val eventViewModel: FinishedViewModel by viewModels()
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeEvents()
        observeLoading()
        observeError()

        // Load finished events
        eventViewModel.getFinishedEvents()
    }

    private fun setupRecyclerView() {
        binding.rvFinished.layoutManager = LinearLayoutManager(context)
        binding.rvFinished.setHasFixedSize(true)
        eventAdapter = EventAdapter()
        binding.rvFinished.adapter = eventAdapter
    }

    private fun observeEvents() {
        eventViewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
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

            // Refresh data
            eventViewModel.getFinishedEvents()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

