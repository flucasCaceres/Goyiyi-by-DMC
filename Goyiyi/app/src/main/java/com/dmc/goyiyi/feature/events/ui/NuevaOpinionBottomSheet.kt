package com.dmc.goyiyi.feature.events.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.dmc.goyiyi.databinding.BottomsheetNuevaOpinionBinding
import com.dmc.goyiyi.feature.events.vm.NuevaOpinionViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NuevaOpinionBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomsheetNuevaOpinionBinding
    private val viewModel: NuevaOpinionViewModel by viewModels()

    companion object {
        fun newInstance(idEvento: String): NuevaOpinionBottomSheet {
            val frag = NuevaOpinionBottomSheet()
            frag.arguments = Bundle().apply {
                putString("idEvento", idEvento)
            }
            return frag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetNuevaOpinionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val idEvento = arguments?.getString("idEvento") ?: return

        binding.btnEnviarOpinion.setOnClickListener {
            println("🔥 BOTTOMSHEET: Botón ENVIAR presionado")
            val comentario = binding.edtComentario.text.toString().trim()
            val rating = binding.ratingBar.rating.toInt()
            println("🔥 BOTTOMSHEET: rating=$rating comentario=$comentario")

            if (rating == 0) {
                Toast.makeText(requireContext(), "Selecciona una valoración", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (comentario.isBlank()) {
                Toast.makeText(requireContext(), "Escribe un comentario", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.enviarOpinion(
                idEvento = idEvento,
                comentario = comentario,
                valoracion = rating
            )
        }

        viewModel.resultado.observe(viewLifecycleOwner) { exito ->
            if (exito == true) {
                Toast.makeText(requireContext(), "Opinión publicada", Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
    }
}
