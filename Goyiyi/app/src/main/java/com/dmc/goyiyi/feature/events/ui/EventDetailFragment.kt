package com.dmc.goyiyi.feature.events.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dmc.goyiyi.databinding.FragmentEventDetailBinding
import com.dmc.goyiyi.feature.events.data.model.Event
import com.dmc.goyiyi.feature.events.vm.DetailUiState
import com.dmc.goyiyi.feature.events.vm.EventDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventDetailFragment : Fragment() {

    private val args: EventDetailFragmentArgs by navArgs()
    private val viewModel: EventDetailViewModel by viewModels()

    private var _binding: FragmentEventDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // navegación hacia atrás
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // cargar evento
        viewModel.loadEvent(args.eventId)

        // observar estado
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is DetailUiState.Success -> showDetail(state.event)
                    is DetailUiState.Loading -> {
                        // TODO: mostrar shimmer / progress si querés
                    }
                    is DetailUiState.Error -> {
                        // TODO: mostrar mensaje de error en pantalla
                    }
                }
            }
        }
    }

    private fun showDetail(event: Event) = with(binding) {
        // Datos básicos (lo que ya tenías)
        title.text = event.nombre
        description.text = event.descripcion
        organizador.text = "Organizador: ${event.organizador ?: "..."}"
        estado.text = "Estado: ${event.estado ?: "..."}"
        tipo.text = "Tipo: ${event.tipo ?: "..." }"

        // Nuevos datos del JSON
        // apto: +8 AÑOS
        apto.text = "Apto: ${event.apto}"

        // Contador de cupos / participantes
        // ejemplo: "Entradas: 60 / 200 (mín 10)"
        cupo.text = "Entradas: ${event.contEntradasVendidas} / ${event.cupoMax}"

        // Precio + moneda
        precio.text = "${event.moneda} ${event.precio}"

        metodoPago.text = "Método de pago: ${event.metodoPago}"

        // Likes / dislikes
        likes.text = "👍 ${event.contLikes}"
        dislikes.text = "👎 ${event.contDislikes}"

        // Comentarios: por ahora solo placeholders, cuando tengas el modelo se enchufa acá
        verTodosComentarios.setOnClickListener {
            // TODO: navegar a una pantalla de "todas las reseñas" tipo MercadoLibre
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
