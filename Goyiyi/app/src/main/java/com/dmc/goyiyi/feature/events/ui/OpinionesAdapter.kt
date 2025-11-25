package com.dmc.goyiyi.feature.events.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dmc.goyiyi.databinding.ItemOpinionBinding
import com.dmc.goyiyi.feature.events.data.model.Opinion
import com.dmc.goyiyi.R

class OpinionesAdapter(
    private var items: List<Opinion>
) : RecyclerView.Adapter<OpinionesAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemOpinionBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Opinion) = with(binding) {
            txtValoracion.text = "★" + item.valoracion
            txtComentario.text = item.comentarios
            txtFecha.text = item.fechaCreacion.take(10) // YYYY-MM-DD

            Glide.with(binding.root.context)
                .load(item.img)
                .placeholder(R.drawable.ic_profile_placeholder)
                .into(binding.imgUsuario)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemOpinionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun updateData(newList: List<Opinion>) {
        items = newList
        notifyDataSetChanged()
    }
}
