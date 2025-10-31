package com.dmc.goyiyi.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dmc.goyiyi.R
import com.dmc.goyiyi.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNav: BottomNavigationView = binding.bottomNav
        bottomNav.setupWithNavController(navController)

        // Mostrar/ocultar bottomNav según destino
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val showBottom = when (destination.id) {
                R.id.events_dest,
                R.id.home_dest,
                R.id.map_dest,
                R.id.profile_dest -> true
                R.id.loginFragment,
                R.id.registerFragment -> false
                else -> false
            }

            binding.bottomNav.isVisible = showBottom
            if (!showBottom) {
                // opcional: “limpia” selección para que no quede un tab marcado al volver a login
                binding.bottomNav.menu.setGroupCheckable(0, true, true)
                binding.bottomNav.clearFocus()
            }
        }

        // Reselección sin recargar
        bottomNav.setOnItemReselectedListener { /* no-op */ }
        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.home_dest
        }

        // Listener del NavigationView del drawer (mantiene tus TOASTS)
        val endNavView: NavigationView = binding.endNavView
        endNavView.setNavigationItemSelectedListener { item ->
            val handled = when (item.itemId) {
                R.id.action_edit_profile -> {
                    Toast.makeText(this, "Editando perfil...", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_logout -> {
                    Toast.makeText(this, "Saliendo de la cuenta...", Toast.LENGTH_SHORT).show()
                    // Reset al graph de AUTH (vuelve al login y limpia back stack)
                    navController.setGraph(R.navigation.nav_graph_auth)
                    true
                }
                R.id.action_exit -> {
                    Toast.makeText(this, "Saliendo de la aplicacion...", Toast.LENGTH_SHORT).show()
                    finishAffinity()
                    true
                }
                else -> false
            }
            binding.drawerLayout.closeDrawer(GravityCompat.END)
            handled
        }
    }

    override fun onBackPressed() {
        // Cierra el drawer antes de salir/navegar hacia atrás
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.END)) {
            binding.drawerLayout.closeDrawer(GravityCompat.END)
        } else {
            super.onBackPressed()
        }
    }
}
