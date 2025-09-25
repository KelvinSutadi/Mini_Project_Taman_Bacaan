package com.example.mini_project_taman_bacaan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mini_project_taman_bacaan.databinding.FragmentAdminAccountBinding

class AdminAccountFragment : Fragment() {

    private var _binding: FragmentAdminAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAdminAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Logika untuk Tombol Tambah User
        binding.addUserButton.setOnClickListener {
            val username = binding.newUsernameEditText.text.toString().trim()
            val password = binding.newPasswordEditText.text.toString().trim()
            val role = binding.roleSpinner.selectedItem.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val success = UserManager.addUser(username, password, role)
                if (success) {
                    Toast.makeText(context, "User '$username' berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                    binding.newUsernameEditText.text.clear()
                    binding.newPasswordEditText.text.clear()
                } else {
                    Toast.makeText(context, "Gagal, username '$username' sudah ada.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Username dan password tidak boleh kosong.", Toast.LENGTH_SHORT).show()
            }
        }

        // Logika untuk Tombol Logout
        binding.logoutButton.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}