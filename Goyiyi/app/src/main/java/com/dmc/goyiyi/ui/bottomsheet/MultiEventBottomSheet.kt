package com.dmc.goyiyi.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.dmc.goyiyi.databinding.BottomsheetMultiEventBinding
import com.dmc.goyiyi.databinding.ItemMultiEventBinding
import com.dmc.goyiyi.feature.map.data.model.EventMapUiModel

class MultiEventBottomSheet(
    private val events: List<EventMapUiModel>,
    private val onEventSelected: (EventMapUiModel) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomsheetMultiEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomsheetMultiEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.multiEventList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = MultiEventAdapter(events) { selected ->
                onEventSelected(selected)
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    class MultiEventAdapter(
        private val data: List<EventMapUiModel>,
        private val onClick: (EventMapUiModel) -> Unit
    ) : RecyclerView.Adapter<MultiEventAdapter.ViewHolder>() {

        inner class ViewHolder(val binding: ItemMultiEventBinding)
            : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemMultiEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ViewHolder(binding)
        }

        override fun getItemCount(): Int = data.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val event = data[position]

            holder.binding.eventName.text = event.nombre
            holder.binding.eventSubtitle.text =
                "${event.estado} • ${event.tipo ?: "Evento"}"

            // TODO: Si querés mostrar iconos reales por estado, te los genero
            holder.binding.eventStatusIcon.setImageResource(
                getIconForState(event.estado)
            )

            holder.binding.root.setOnClickListener { onClick(event) }
        }

        private fun getIconForState(estado: String): Int {
            return when (estado.uppercase()) {
                "CANCELADO" -> com.dmc.goyiyi.R.drawable.ic_pin_cancelado
                "REPROGRAMADO" -> com.dmc.goyiyi.R.drawable.ic_pin_reprogramado
                "SUCEDIENDO" -> com.dmc.goyiyi.R.drawable.ic_pin_sucediendo
                "PAUSADO", "FINALIZADO", "PROGRAMADO" -> com.dmc.goyiyi.R.drawable.ic_pin_pausado
                else -> com.dmc.goyiyi.R.drawable.ic_pin_pausado
            }
        }
    }
}
