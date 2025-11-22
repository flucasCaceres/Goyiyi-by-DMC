package com.dmc.goyiyi.feature.events.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmc.goyiyi.core.util.Resource
import com.dmc.goyiyi.databinding.FragmentEventsBinding
import com.dmc.goyiyi.feature.events.vm.EventsViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.dmc.goyiyi.feature.events.vm.SortOrder
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController

@AndroidEntryPoint
class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!
    private val vm: EventsViewModel by viewModels()
    private lateinit var adapter: EventsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = EventsAdapter { event ->
            val action =
                EventsFragmentDirections.actionEventsDestToEventDetailDest(event.id)
            findNavController().navigate(action)
        }

        binding.recyclerEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerEvents.adapter = adapter


        binding.btnFiltro.setOnClickListener {
            val opciones = arrayOf("Popularidad", "Precio ↑", "Precio ↓", "Cupo", "Nombre A-Z")
            AlertDialog.Builder(requireContext())
                .setTitle("Ordenar por")
                .setItems(opciones) { _, which ->
                    val orden = when (which) {
                        0 -> SortOrder.POPULARIDAD
                        1 -> SortOrder.PRECIO_ASC
                        2 -> SortOrder.PRECIO_DESC
                        3 -> SortOrder.CUPO_DESC
                        4 -> SortOrder.NOMBRE_ASC
                        else -> SortOrder.POPULARIDAD
                    }
                    vm.setSort(orden)
                }
                .show()
        }

        lifecycleScope.launch {
            vm.uiState.collectLatest { state ->
                binding.progressEvents.visibility = if (state.isLoading) View.VISIBLE else View.GONE

                state.error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }

                adapter.submitList(state.visible)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
