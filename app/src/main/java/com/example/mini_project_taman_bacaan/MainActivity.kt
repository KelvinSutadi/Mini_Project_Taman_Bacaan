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

        val userRole = intent.getStringExtra("USER_ROLE")

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Memilih menu dan grafik navigasi berdasarkan peran
        if (userRole == "ADMIN") {
            // Memuat peta navigasi admin
            navController.setGraph(R.navigation.nav_graph_admin)
            // Menampilkan menu admin
            binding.bottomNavigation.inflateMenu(R.menu.bottom_nav_menu_admin)
        } else {
            // Memuat peta navigasi user
            navController.setGraph(R.navigation.nav_graph_user)
            // Menampilkan menu user
            binding.bottomNavigation.inflateMenu(R.menu.bottom_nav_menu_user)
        }

        // Hubungkan bottom navigation dengan controller
        binding.bottomNavigation.setupWithNavController(navController)
    }
}