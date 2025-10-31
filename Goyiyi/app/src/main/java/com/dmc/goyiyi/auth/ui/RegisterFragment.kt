package com.dmc.goyiyi.auth.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dmc.goyiyi.R
import com.dmc.goyiyi.auth.vm.RegisterViewModel
import com.dmc.goyiyi.databinding.FragmentRegisterBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val uiFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
    private val isoFmt = DateTimeFormatter.ISO_LOCAL_DATE
    private val DOB_TAG = "fecha_nacimiento_picker"

    private val vm: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentRegisterBinding.bind(view)

        // Pre-cargar fecha si existe
        vm.fechaNacimientoIso.observe(viewLifecycleOwner) { iso ->
            binding.etFechaNacimiento.setText(
                iso?.let { LocalDate.parse(it, isoFmt).format(uiFmt) } ?: ""
            )
        }

        // Abrir calendario
        binding.etFechaNacimiento.setOnClickListener { abrirDatePicker() }
        binding.tilFechaNacimiento.setEndIconOnClickListener { abrirDatePicker() }

        // Volver a Login mostrando spinner
        binding.btnRegistrarse.setOnClickListener { volverALoginConSpinner() }
        binding.tvIniciarSesion.setOnClickListener { volverALoginConSpinner() }
    }

    override fun onDestroyView() {
        ocultarCarga()
        _binding = null
        super.onDestroyView()
    }

    // --- Navegación a Login con overlay ---
    private fun volverALoginConSpinner() {
        if (!binding.btnRegistrarse.isEnabled) return
        binding.btnRegistrarse.isEnabled = false
        binding.tvIniciarSesion.isEnabled = false
        mostrarCarga()
        // Como Register fue abierto desde Login, con un pop alcanza
        findNavController().popBackStack()
    }

    private fun mostrarCarga() {
        binding.overlaySpinner.root.bringToFront()
        binding.overlaySpinner.root.visibility = View.VISIBLE
    }
    private fun ocultarCarga() {
        binding.overlaySpinner.root.visibility = View.GONE
    }

    // --- DatePicker ---
    private fun abrirDatePicker() {
        val preSelectedLocal: LocalDate? = vm.fechaNacimientoIso.value?.let {
            LocalDate.parse(it, isoFmt)
        }
        val preSelectedUtcMillis: Long? = preSelectedLocal
            ?.atStartOfDay(ZoneOffset.UTC)
            ?.toInstant()
            ?.toEpochMilli()

        val constraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())
            .build()

        val builder = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Seleccioná tu fecha de nacimiento")
            .setCalendarConstraints(constraints)

        preSelectedUtcMillis?.let { builder.setSelection(it) }

        val picker = builder.build()

        picker.addOnPositiveButtonClickListener { utcMillis ->
            val pickedLocalDate: LocalDate = Instant.ofEpochMilli(utcMillis)
                .atZone(ZoneOffset.UTC)
                .toLocalDate()

            binding.etFechaNacimiento.setText(pickedLocalDate.format(uiFmt))
            vm.setFechaNacimientoIso(pickedLocalDate.format(isoFmt))
        }

        if (childFragmentManager.findFragmentByTag(DOB_TAG) == null) {
            picker.show(childFragmentManager, DOB_TAG)
        }
    }
}
