package com.dmc.goyiyi.auth.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dmc.goyiyi.R
import com.dmc.goyiyi.auth.vm.RegisterState
import com.dmc.goyiyi.auth.vm.RegisterViewModel
import com.dmc.goyiyi.databinding.FragmentRegisterBinding
import com.dmc.goyiyi.util.asLoadingOverlay
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import com.google.android.material.textfield.TextInputLayout


@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val vm: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentRegisterBinding.bind(view)

        val loading = binding.overlaySpinner.root.asLoadingOverlay()
        loading.hide()

        // --- BOTÓN REGISTRARSE ---
        binding.btnRegistrarse.setOnClickListener {
            limpiarErrores()
            val nombre = binding.etUsuario.text.toString().trim()
            val correo = binding.etCorreo.text.toString().trim()
            val pass = binding.etContrasena.text.toString()
            val confPass = binding.etConfirmarContrasena.text.toString()

            var hayError = false

            if (nombre.isBlank()) {
                binding.tilUsuario.error = "Ingresá un nombre de usuario"
                hayError = true
            }

            if (correo.isBlank()) {
                binding.tilCorreo.error = "Ingresá un correo electrónico"
                hayError = true
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                binding.tilCorreo.error = "Ingresá un correo válido"
                hayError = true
            }

            if (pass.isBlank()) {
                binding.tilContrasena.error = "Ingresá una contraseña"
                manejarIconoContrasena()
                hayError = true
            } else if (pass.length < 6) {
                binding.tilContrasena.error = "Mínimo 6 caracteres"
                manejarIconoContrasena()
                hayError = true
            }


            if (confPass.isBlank()) {
                binding.tilConfirmarContrasena.error = "Repetí la contraseña"
                hayError = true
            } else if (pass != confPass) {
                binding.tilConfirmarContrasena.error = "Las contraseñas no coinciden"
                hayError = true
            }

            if (hayError) {
                Toast.makeText(requireContext(), "Por favor completá todos los campos correctamente", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            binding.tilContrasena.error = null
            manejarIconoContrasena()
            // Si pasó todas las validaciones de UI, recién ahí llamamos al ViewModel
            vm.register(nombre, correo, pass, confPass)
        }


        // --- IR A LOGIN ---
        binding.tvIniciarSesion.setOnClickListener { volverALoginConSpinner(loading) }

        // --- OBSERVAR ESTADOS ---
        lifecycleScope.launchWhenStarted {
            vm.state.collectLatest { state ->
                when (state) {
                    is RegisterState.Idle -> Unit

                    is RegisterState.Loading -> {
                        loading.show()
                        binding.btnRegistrarse.isEnabled = false
                        binding.tvIniciarSesion.isEnabled = false
                    }

                    is RegisterState.Success -> {
                        loading.hide()
                        Toast.makeText(requireContext(), "Cuenta creada exitosamente", Toast.LENGTH_LONG).show()
                        findNavController().popBackStack()
                    }

                    is RegisterState.Error -> {
                        loading.hide()
                        Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
                        binding.btnRegistrarse.isEnabled = true
                        binding.tvIniciarSesion.isEnabled = true
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun limpiarErrores() {
        binding.tilUsuario.error = null
        binding.tilCorreo.error = null
        binding.tilContrasena.error = null
        binding.tilConfirmarContrasena.error = null
        manejarIconoContrasena()
    }
    private fun manejarIconoContrasena() {
        val error = binding.tilContrasena.error

        if (error.isNullOrEmpty()) {
            binding.tilContrasena.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        } else {
            binding.tilContrasena.endIconMode = TextInputLayout.END_ICON_NONE
        }
    }

    private fun volverALoginConSpinner(loading: com.dmc.goyiyi.util.LoadingOverlay) {
        binding.btnRegistrarse.isEnabled = false
        binding.tvIniciarSesion.isEnabled = false
        loading.show()

        binding.root.postDelayed({
            findNavController().popBackStack()
        }, 250)
    }
}
