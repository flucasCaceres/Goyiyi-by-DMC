package com.dmc.goyiyi.feature.map.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.dmc.goyiyi.databinding.BottomsheetEventBinding
import com.dmc.goyiyi.feature.map.data.model.EventMapUiModel

class EventBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomsheetEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomsheetEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nombre = arguments?.getString("nombre") ?: ""
        val estado = arguments?.getString("estado") ?: ""
        val tipo = arguments?.getString("tipo") ?: ""
        val organizador = arguments?.getString("organizador") ?: ""
        val likes = arguments?.getInt("likes") ?: 0
        val dislikes = arguments?.getInt("dislikes") ?: 0

        binding.eventName.text = nombre
        binding.eventEstado.text = estado
        binding.eventTipo.text = tipo
        binding.eventOrganizador.text = organizador
        binding.eventRating.text = "$likes 👍   $dislikes 👎"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance(event: EventMapUiModel): EventBottomSheet {
            val sheet = EventBottomSheet()
            sheet.arguments = Bundle().apply {
                putString("nombre", event.nombre)
                putString("estado", event.estado)
                putString("tipo", event.tipo)
                putString("organizador", event.organizador)
                putInt("likes", event.likes)
                putInt("dislikes", event.dislikes)
            }
            return sheet
        }
    }
}
