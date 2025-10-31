package com.dmc.goyiyi.feature.profile.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.dmc.goyiyi.R
import com.dmc.goyiyi.databinding.FragmentProfileBinding
import androidx.core.view.GravityCompat

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        setupToolbarMenu()
    }

    private fun setupToolbarMenu() {
        binding.profileToolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_menu -> {
                    // Abrimos el drawer que vive en la Activity
                    val drawer = requireActivity()
                        .findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawerLayout)
                    drawer.openDrawer(GravityCompat.END)
                    true
                }
                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
