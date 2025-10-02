package com.example.mini_project_taman_bacaan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mini_project_taman_bacaan.databinding.FragmentAdminAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminAccountFragment : Fragment() {

    private var _binding: FragmentAdminAccountBinding? = null
    private val binding get() = _binding!!

    // Inisialisasi Firebase Auth dan Firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAdminAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi instance Firebase
        auth = Firebase.auth
        db = Firebase.firestore

        val usernameFromLogin = requireActivity().intent.getStringExtra("USERNAME") ?: "Admin"
        binding.accountInfoTextView.text = "Akun Admin: $usernameFromLogin"

        // Logika untuk Tombol Tambah User (DIGANTI DENGAN FIREBASE)
        binding.addUserButton.setOnClickListener {
            val email = binding.newUsernameEditText.text.toString().trim() // Sekarang ini adalah email
            val password = binding.newPasswordEditText.text.toString().trim()
            val role = binding.roleSpinner.selectedItem.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Buat akun baru di Firebase Authentication
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Jika akun berhasil dibuat, simpan perannya di Firestore
                            val userId = task.result?.user?.uid
                            if (userId != null) {
                                val userMap = hashMapOf(
                                    "role" to role,
                                    "email" to email
                                )
                                // Simpan data peran ke koleksi "users" dengan ID dari Auth
                                db.collection("users").document(userId).set(userMap)
                                    .addOnSuccessListener {
                                        Toast.makeText(context, "User '$email' dengan peran '$role' berhasil dibuat!", Toast.LENGTH_SHORT).show()
                                        binding.newUsernameEditText.text.clear()
                                        binding.newPasswordEditText.text.clear()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(context, "Gagal menyimpan peran: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            // Jika gagal membuat akun (misal: email sudah terdaftar)
                            Toast.makeText(context, "Gagal membuat akun: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            } else {
                Toast.makeText(context, "Email dan password tidak boleh kosong.", Toast.LENGTH_SHORT).show()
            }
        }

        // Logika untuk Tombol Logout (tetap sama)
        binding.logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah anda yakin ingin log out?")
            .setPositiveButton("Ya") { _, _ -> logout() }
            .setNegativeButton("Tidak", null)
            .show()
    }

    private fun logout() {
        // Logout dari Firebase sebelum kembali ke LoginActivity
        auth.signOut()
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}