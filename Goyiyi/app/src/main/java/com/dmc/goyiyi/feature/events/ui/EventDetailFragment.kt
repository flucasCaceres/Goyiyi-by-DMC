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
import com.dmc.goyiyi.feature.events.data.model.Opinion
import com.dmc.goyiyi.feature.events.vm.DetailUiState
import com.dmc.goyiyi.feature.events.vm.EventDetailViewModel
import com.dmc.goyiyi.R
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
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.opinionesPreview.collect { opiniones ->
                renderOpinionesPreview(opiniones)
            }
        }

    }
    private fun renderOpinionesPreview(opiniones: List<Opinion>) {
        with(binding) {

            // Sin opiniones → ocultamos todo
            if (opiniones.isEmpty()) {
                comentario1.visibility = View.VISIBLE
                comentario1.text = "Nadie opinó todavía"
                comentario2.visibility = View.GONE

                verTodosComentarios.isEnabled = true
                verTodosComentarios.text = "Ver todos los comentarios"
                return
            }


            // Hay opiniones
            verTodosComentarios.isEnabled = true
            verTodosComentarios.text = "Ver todos los comentarios"

            // Primera opinión
            val first = opiniones.getOrNull(0)
            if (first != null) {
                comentario1.visibility = View.VISIBLE
                comentario1.text = "★${first.valoracion}  ${first.comentarios.orEmpty()}"
            } else {
                comentario1.visibility = View.GONE
            }

            // Segunda opinión
            val second = opiniones.getOrNull(1)
            if (second != null) {
                comentario2.visibility = View.VISIBLE
                comentario2.text = "★${second.valoracion}  ${second.comentarios.orEmpty()}"
            } else {
                comentario2.visibility = View.GONE
            }
        }
    }

    private fun showDetail(event: Event) = with(binding) {
        // Datos básicos (lo que ya tenías)
        title.text = event.nombre ?: "Sin nombre"
        description.text = event.descripcion
            ?.takeIf { it.isNotBlank() }
            ?: "Sin descripción disponible"
        organizador.text = "Organizador: ${event.organizador ?: "No especificado"}"
        estado.text = event.estado ?: "Sin estado"
        tipo.text = event.tipo ?: "No especificado"

        // Apto para
        apto.text = "Apto: ${event.apto ?: "No especificado"}"

        // Contador de cupos / participantes
        // ejemplo: "Entradas: 60 / 200 (mín 10)"
        cupo.text = "Entradas: ${event.contEntradasVendidas ?: 0} / ${event.cupoMax ?: 0}"

        // Precio + moneda
        val moneda = event.moneda ?: "ARS"
        val precioValor = event.precio ?: 0
        precio.text = "$moneda $precioValor"

        // Método de pago
        metodoPago.text = "Método de pago: ${event.metodoPago ?: "No especificado"}"

        // Likes / dislikes
        likes.apply {
            text = (event.contLikes ?: 0).toString()
            setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_thumb_up, 0, 0, 0
            )
        }

        dislikes.apply {
            text = (event.contDislikes ?: 0).toString()
            setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_thumb_down, 0, 0, 0
            )
        }


        eventImage.setImageResource(android.R.color.darker_gray)
        // Comentarios: por ahora solo placeholders, cuando tengas el modelo se enchufa acá
        verTodosComentarios.setOnClickListener {
            val action = EventDetailFragmentDirections
                .actionEventDetailFragmentToOpinionesFragment(args.eventId)

            findNavController().navigate(action)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadEvent(args.eventId)
    }



}
