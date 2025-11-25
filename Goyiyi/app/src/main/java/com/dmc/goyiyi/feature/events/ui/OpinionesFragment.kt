package com.dmc.goyiyi.feature.events.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmc.goyiyi.databinding.FragmentOpinionesBinding
import com.dmc.goyiyi.feature.events.vm.OpinionesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OpinionesFragment : Fragment() {

    private val args: OpinionesFragmentArgs by navArgs()
    private val viewModel: OpinionesViewModel by viewModels()

    private var _binding: FragmentOpinionesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: OpinionesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpinionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = OpinionesAdapter(emptyList())

        binding.opinionesRecycler.layoutManager =
            LinearLayoutManager(requireContext())

        binding.opinionesRecycler.adapter = adapter

        binding.btnEscribirOpinion.setOnClickListener {
          val dialog = NuevaOpinionBottomSheet.newInstance(args.idEvento)
            dialog.show(parentFragmentManager, "NuevaOpinion")
        }


        viewModel.loadOpiniones(args.idEvento)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.opiniones.collect { lista ->
                if (lista.isEmpty()) {
                    binding.emptyMessage.visibility = View.VISIBLE
                    binding.opinionesRecycler.visibility = View.GONE
                } else {
                    binding.emptyMessage.visibility = View.GONE
                    binding.opinionesRecycler.visibility = View.VISIBLE
                    adapter.updateData(lista)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadOpiniones(args.idEvento)
    }



}
