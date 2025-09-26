package com.example.mini_project_taman_bacaan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mini_project_taman_bacaan.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            val user = UserManager.login(username, password)

            if (user != null) {
                // Login berhasil → buka MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("USER_ROLE", user.role) // kirim role (admin/user)
                intent.putExtra("USERNAME", username)    // kirim username
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Username atau password salah", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
