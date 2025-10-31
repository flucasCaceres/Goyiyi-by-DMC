package com.dmc.goyiyi.auth.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import com.dmc.goyiyi.databinding.FragmentLoginBinding
import com.dmc.goyiyi.R
import androidx.navigation.fragment.findNavController
import com.dmc.goyiyi.ui.MainActivity

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private fun mostrarCarga() {
        binding.overlaySpinner.root.bringToFront()
        binding.overlaySpinner.root.visibility = View.VISIBLE
    }
    private fun ocultarCarga() {
        binding.overlaySpinner.root.visibility = View.GONE
    }

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(i, c, false)
        return binding.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        ocultarCarga()

        binding.btnLogin.setOnClickListener {
            mostrarCarga()
            val opts = ActivityOptionsCompat.makeCustomAnimation(
                requireContext(), android.R.anim.fade_in, android.R.anim.fade_out
            )
            // si tenés lógica async, llamá ocultarCarga() en su callback
            startActivity(Intent(requireContext(), MainActivity::class.java), opts.toBundle())
            binding.root.post { requireActivity().finish() }
        }

        binding.txtRegistrarse.setOnClickListener {
            if (!binding.txtRegistrarse.isEnabled) return@setOnClickListener
            binding.txtRegistrarse.isEnabled = false
            mostrarCarga()
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onResume() {
        super.onResume();
        ocultarCarga()
    }

    override fun onDestroyView() {
        // por si quedó visible al navegar:
        ocultarCarga()
        _binding = null
        super.onDestroyView()
    }
}
