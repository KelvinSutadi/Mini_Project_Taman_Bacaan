package com.example.mini_project_taman_bacaan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mini_project_taman_bacaan.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari LoginActivity
        val userRole = intent.getStringExtra("USER_ROLE") ?: "USER"
        val username = intent.getStringExtra("USERNAME") ?: "Guest"

        // Siapkan bundle untuk kirim username ke fragment awal
        val bundle = Bundle().apply {
            putString("username", username)
        }

        // Ambil NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Pilih graph sesuai role
        if (userRole.equals("ADMIN", ignoreCase = true)) {
            navController.setGraph(R.navigation.nav_graph_admin, bundle)
        } else {
            navController.setGraph(R.navigation.nav_graph_user, bundle)
        }

        // Setup BottomNavigation dengan NavController
        binding.bottomNavigation.inflateMenu(R.menu.bottom_nav_menu)
        binding.bottomNavigation.setupWithNavController(navController)
    }
}
