package com.example.dicodingeventapp.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
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

        eventViewModel.getFinishedEvents()
    }

    private fun setupRecyclerView() {
        binding.rvFinished.layoutManager = LinearLayoutManager(context)
        binding.rvFinished.setHasFixedSize(true)
        eventAdapter = EventAdapter()
        binding.rvFinished.adapter = eventAdapter
    }

    private fun observeEvents() {
        eventViewModel.events.observe(viewLifecycleOwner) { events ->
            eventAdapter.submitList(events)
        }
    }

    private fun observeLoading() {
        eventViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

