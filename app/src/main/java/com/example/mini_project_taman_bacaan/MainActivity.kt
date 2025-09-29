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
        val username = intent.getStringExtra("USERNAME")

        val bundle = Bundle()
        bundle.putString("username", username)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        if (userRole == "ADMIN") {
            navController.setGraph(R.navigation.nav_graph_admin, bundle)
        } else {
            navController.setGraph(R.navigation.nav_graph_user, bundle)
        }

        binding.bottomNavigation.inflateMenu(R.menu.bottom_nav_menu)
        binding.bottomNavigation.setupWithNavController(navController)
    }
}