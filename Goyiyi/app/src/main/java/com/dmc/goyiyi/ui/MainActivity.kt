package com.dmc.goyiyi.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dmc.goyiyi.R
import com.dmc.goyiyi.databinding.ActivityMainBinding
import com.dmc.goyiyi.util.LoadingOverlay
import com.dmc.goyiyi.util.asLoadingOverlay
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var loadingOverlay: LoadingOverlay

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    fun showLoading() {
        loadingOverlay.show()
    }

    fun hideLoading() {
        loadingOverlay.hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingOverlay = binding.overlaySpinner.root.asLoadingOverlay()

        // NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Bottom Navigation setup
        val bottomNav: BottomNavigationView = binding.bottomNav
        bottomNav.setupWithNavController(navController)

        // Listener para mostrar/ocultar bottomNav y bloquear Drawer
        navController.addOnDestinationChangedListener { _, destination, _ ->

            // Mostrar u ocultar bottom nav
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
                // Limpieza opcional para evitar selecciones incorrectas
                binding.bottomNav.menu.setGroupCheckable(0, true, true)
                binding.bottomNav.clearFocus()
            }

            // Bloqueo del Drawer (solo abierto en profile)
            when (destination.id) {
                R.id.profile_dest -> {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                }
                else -> {
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }
        }

        // Listener del NavigationView del Drawer
        val endNavView: NavigationView = binding.endNavView
        endNavView.setNavigationItemSelectedListener { item ->
            val handled = when (item.itemId) {
                R.id.action_edit_profile -> {
                    Toast.makeText(this, "Editando perfil...", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.action_logout -> {
                    Toast.makeText(this, "Saliendo de la cuenta...", Toast.LENGTH_SHORT).show()
                    showLoading()
                    binding.root.postDelayed({
                        hideLoading()
                    }, 350)
                    binding.root.postDelayed({
                        // 1) Ocultar drawer
                        binding.drawerLayout.closeDrawer(GravityCompat.END)

                        // 2) Navegar al graph de auth
                        val navInflater = navController.navInflater
                        val authGraph = navInflater.inflate(R.navigation.nav_graph_auth)
                        navController.graph = authGraph

                    }, 300) // MISMO delay que hideLoading()
                    true
                }
                R.id.action_exit -> {
                    Toast.makeText(this, "Saliendo de la aplicación...", Toast.LENGTH_SHORT).show()
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
