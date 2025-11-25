package com.dmc.goyiyi.feature.events.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.navigation.fragment.findNavController
import com.dmc.goyiyi.util.LoadingOverlay
import com.dmc.goyiyi.util.asLoadingOverlay

@AndroidEntryPoint
class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!
    private val vm: EventsViewModel by viewModels()
    private lateinit var adapter: EventsAdapter
    private lateinit var loadingOverlay: LoadingOverlay

    private fun mostrarCarga() {
        loadingOverlay.show()
    }

    private fun ocultarCarga() {
        loadingOverlay.hide()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingOverlay = binding.overlaySpinner.root.asLoadingOverlay()
        mostrarCarga()
        vm.loadEvents()

        adapter = EventsAdapter { event ->
            val action =
                EventsFragmentDirections.actionEventsDestToEventDetailDest(event.id)
            findNavController().navigate(action)
        }
        binding.swipeRefresh.setOnRefreshListener {
            vm.loadEvents()   // recarga manual estilo Instagram
        }
        binding.recyclerEvents.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerEvents.adapter = adapter

        binding.searchEditText.addTextChangedListener { editable ->
            vm.setQuery(editable?.toString().orEmpty())
        }
        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                vm.setQuery(binding.searchEditText.text.toString())
                binding.searchEditText.clearFocus()
                true
            } else {
                false
            }
        }


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
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.uiState.collectLatest { state ->
                    val isLoading = state.isLoading

                    if (isLoading && !binding.swipeRefresh.isRefreshing) {
                        mostrarCarga()
                    } else {
                        ocultarCarga()
                    }

                    if (!isLoading && binding.swipeRefresh.isRefreshing) {
                        binding.swipeRefresh.isRefreshing = false
                    }
                }
            }
        }


        lifecycleScope.launch {
            vm.uiState.collectLatest { state ->
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
