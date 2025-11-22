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
import com.dmc.goyiyi.util.LoadingOverlay
import com.dmc.goyiyi.util.asLoadingOverlay

class LoginFragment : Fragment() {

    private lateinit var loadingOverlay: LoadingOverlay
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private fun mostrarCarga() = loadingOverlay.show()
    private fun ocultarCarga() = loadingOverlay.hide()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(i, c, false)
        return binding.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        loadingOverlay = binding.overlaySpinner.root.asLoadingOverlay()
        loadingOverlay.hide() // ocultarlo apenas empieza
        binding.btnLogin.setOnClickListener {
            mostrarCarga()
            binding.btnLogin.isEnabled = false

            binding.root.postDelayed({
                val opts = ActivityOptionsCompat.makeCustomAnimation(
                    requireContext(), android.R.anim.fade_in, android.R.anim.fade_out
                )
                startActivity(Intent(requireContext(), MainActivity::class.java), opts.toBundle())

                requireActivity().finish()
            }, 300) // 300ms = tiempo adecuado para que se vea el spinner

        }

        binding.txtRegistrarse.setOnClickListener {
            if (!binding.txtRegistrarse.isEnabled) return@setOnClickListener
            binding.txtRegistrarse.isEnabled = false
            mostrarCarga()
            binding.root.postDelayed({
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }, 250)
        }
    }

    override fun onDestroyView() {
        // por si quedó visible al navegar:
        binding.txtRegistrarse.isEnabled = true
        binding.btnLogin.isEnabled = true
        ocultarCarga()
        _binding = null
        super.onDestroyView()
    }
}
