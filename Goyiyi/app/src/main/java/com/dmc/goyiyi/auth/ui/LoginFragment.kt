package com.dmc.goyiyi.auth.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.dmc.goyiyi.databinding.FragmentLoginBinding
import com.dmc.goyiyi.R
import androidx.navigation.fragment.findNavController
import com.dmc.goyiyi.ui.MainActivity
import com.dmc.goyiyi.util.LoadingOverlay
import com.dmc.goyiyi.util.asLoadingOverlay
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import com.dmc.goyiyi.auth.vm.LoginViewModel
import com.dmc.goyiyi.auth.vm.LoginState
import com.google.android.material.textfield.TextInputLayout


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var loadingOverlay: LoadingOverlay
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val vm: LoginViewModel by viewModels()

    private fun mostrarCarga() = loadingOverlay.show()
    private fun ocultarCarga() = loadingOverlay.hide()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(i, c, false)
        return binding.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        loadingOverlay = binding.overlaySpinner.root.asLoadingOverlay()
        loadingOverlay.hide()

        binding.btnLogin.setOnClickListener {

            binding.tvErrorLogin.visibility = View.GONE
            binding.tilCorreo.error = null
            binding.tilContrasena.error = null

            val correo = binding.etCorreo.text.toString()
            val pass = binding.etContrasena.text.toString()

            var error = false

            if (correo.isBlank()) {
                binding.tilCorreo.error = "Ingresá tu correo"
                error = true
            }

            if (pass.isBlank()) {
                binding.tilContrasena.error = "Ingresá tu contraseña"
                manejarIconoContrasena()
                error = true
            }

            if (error) return@setOnClickListener

            binding.tilContrasena.error = null
            manejarIconoContrasena()
            vm.login(correo, pass)
        }

        binding.txtRegistrarse.setOnClickListener {
            if (!binding.txtRegistrarse.isEnabled) return@setOnClickListener
            binding.txtRegistrarse.isEnabled = false
            mostrarCarga()
            binding.root.postDelayed({
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }, 250)
        }

        // --- Observamos el estado del Login ---
        lifecycleScope.launchWhenStarted {
            vm.state.collectLatest { state ->
                when (state) {

                    is LoginState.Idle -> Unit

                    is LoginState.Loading -> {
                        mostrarCarga()
                        binding.btnLogin.isEnabled = false
                        binding.txtRegistrarse.isEnabled = false
                    }

                    is LoginState.Error -> {
                        ocultarCarga()
                        binding.btnLogin.isEnabled = true
                        binding.txtRegistrarse.isEnabled = true

                        binding.tvErrorLogin.text = state.error
                        binding.tvErrorLogin.visibility = View.VISIBLE
                        manejarIconoContrasena()
                    }

                    is LoginState.Success -> {
                        mostrarCarga()

                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
            }
        }
    }

    private fun manejarIconoContrasena() {
        val tieneError = !binding.tilContrasena.error.isNullOrEmpty()

        if (tieneError) {
            binding.tilContrasena.endIconMode = TextInputLayout.END_ICON_NONE
        } else {
            binding.tilContrasena.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        }
    }

    override fun onDestroyView() {
        binding.txtRegistrarse.isEnabled = true
        binding.btnLogin.isEnabled = true
        ocultarCarga()
        _binding = null
        super.onDestroyView()
    }
}
