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

        // 1. Pilih dan atur "peta" navigasi berdasarkan peran
        if (userRole == "ADMIN") {
            navController.setGraph(R.navigation.nav_graph_admin)
        } else {
            navController.setGraph(R.navigation.nav_graph_user)
        }

        // 2. Selalu gunakan SATU menu yang sama untuk keduanya
        binding.bottomNavigation.inflateMenu(R.menu.bottom_nav_menu)

        // 3. Hubungkan bottom navigation dengan controller agar navigasi otomatis berfungsi
        binding.bottomNavigation.setupWithNavController(navController)
    }
}