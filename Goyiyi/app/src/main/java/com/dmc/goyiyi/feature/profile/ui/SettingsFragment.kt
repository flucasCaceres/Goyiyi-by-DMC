package com.dmc.goyiyi.feature.profile.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.dmc.goyiyi.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(i, c, false)
        return binding.root
    }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}
