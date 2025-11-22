package com.dmc.goyiyi.feature.events.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dmc.goyiyi.R
import com.dmc.goyiyi.feature.events.data.model.Event

class EventsAdapter(
    private val onClick: (Event) -> Unit
) : ListAdapter<Event, EventsAdapter.EventViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Event>() {
            override fun areItemsTheSame(oldItem: Event, newItem: Event) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Event, newItem: Event) = oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class EventViewHolder(
        private val view: View,
        private val onClick: (Event) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val nombre: TextView = view.findViewById(R.id.txtNombre)
        private val descripcion: TextView = view.findViewById(R.id.txtDescripcion)
        private val organizador: TextView = view.findViewById(R.id.txtOrganizador)
        private val estado: TextView = view.findViewById(R.id.txtEstado)
        private val tipo: TextView = view.findViewById(R.id.txtTipo)

        private val pago: TextView = view.findViewById(R.id.txtPago)
        private val cupo: TextView = view.findViewById(R.id.txtCupo)
        private val precio: TextView = view.findViewById(R.id.txtPrecio)

        fun bind(event: Event) {

            view.setOnClickListener { onClick(event) }


            nombre.text = event.nombre
            descripcion.text = event.descripcion ?: "Sin descripción"
            organizador.text = "Organiza: ${event.organizador ?: "Desconocido"}"
            estado.text = event.estado ?: "SIN ESTADO"
            tipo.text = event.tipo ?: "Tipo desconocido"
            cupo.text = "Cupo: ${(event.contEntradasVendidas ?: 0)}/${event.cupoMax ?: 0}"
            pago.text = "Pago: ${event.metodoPago ?: "N/A"}"
            val precioText = buildString {
                append(event.precio ?: "0")
                if (!event.moneda.isNullOrBlank()) append(" ${event.moneda}")
            }
            precio.text = precioText
        }
    }
}
