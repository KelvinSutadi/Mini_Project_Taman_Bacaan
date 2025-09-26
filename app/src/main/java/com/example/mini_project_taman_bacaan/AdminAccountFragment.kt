package com.example.mini_project_taman_bacaan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mini_project_taman_bacaan.databinding.FragmentAdminAccountBinding

class AdminAccountFragment : Fragment() {

    private var _binding: FragmentAdminAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DISESUAIKAN: Mengambil username dari intent dan menampilkannya
        val usernameFromLogin = requireActivity().intent.getStringExtra("USERNAME") ?: "Admin"
        binding.accountInfoTextView.text = "Akun Admin: $usernameFromLogin"

        // KODE ANDA: Logika untuk mengisi spinner (sudah benar)
        val roles = resources.getStringArray(R.array.user_roles)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, roles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.roleSpinner.adapter = adapter

        // KODE ANDA: Logika tombol tambah user (sudah benar)
        binding.addUserButton.setOnClickListener {
            val username = binding.newUsernameEditText.text.toString().trim()
            val password = binding.newPasswordEditText.text.toString().trim()
            val role = binding.roleSpinner.selectedItem.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = UserManager.addUser(username, password, role)

            if (success) {
                Toast.makeText(requireContext(), "User $username berhasil ditambahkan", Toast.LENGTH_SHORT).show()

                binding.newUsernameEditText.text.clear()
                binding.newPasswordEditText.text.clear()
                binding.roleSpinner.setSelection(0)
            } else {
                Toast.makeText(requireContext(), "Username sudah terdaftar!", Toast.LENGTH_SHORT).show()
            }
        }

        // KODE ANDA: Logika tombol logout (sudah benar)
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