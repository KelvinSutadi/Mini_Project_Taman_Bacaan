package com.example.mini_project_taman_bacaan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mini_project_taman_bacaan.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val currentUser = auth.currentUser
        if (currentUser != null) {
            fetchUserRoleAndProceed(currentUser.uid, currentUser.email ?: "")
        }

        binding.loginButton.setOnClickListener {
            val email = binding.usernameEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = auth.currentUser
                        if (firebaseUser != null) {
                            fetchUserRoleAndProceed(firebaseUser.uid, email)
                        }
                    } else {
                        Toast.makeText(baseContext, "Login gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // DITAMBAHKAN: Listener untuk teks registrasi
        binding.registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchUserRoleAndProceed(userId: String, email: String) {
        val db = Firebase.firestore
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                var role = "USER"
                if (document != null && document.exists()) {
                    role = document.getString("role") ?: "USER"
                }

                val cleanUsername = email.split("@")[0]
                    .replace(".", " ")
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }

                goToMainActivity(role, cleanUsername)
            }
            .addOnFailureListener {
                Toast.makeText(baseContext, "Gagal mengambil data peran, masuk sebagai User.", Toast.LENGTH_SHORT).show()
                val cleanUsername = email.split("@")[0]
                goToMainActivity("USER", cleanUsername)
            }
    }

    private fun goToMainActivity(role: String, username: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("USER_ROLE", role)
        intent.putExtra("USERNAME", username)
        startActivity(intent)
        finish()
    }
}