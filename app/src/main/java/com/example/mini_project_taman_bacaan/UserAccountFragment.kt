package com.example.mini_project_taman_bacaan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mini_project_taman_bacaan.databinding.FragmentUserAccountBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserAccountFragment : Fragment() {

    private var _binding: FragmentUserAccountBinding? = null
    private val binding get() = _binding!!
    private lateinit var borrowedAdapter: BorrowedBookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = requireActivity().intent.getStringExtra("USERNAME") ?: "User"
        binding.accountInfoTextView.text = "Selamat Datang, $username!"

        setupRecyclerView(username)

        BorrowingManager.borrowedBooksLiveData.observe(viewLifecycleOwner) { borrows ->
            val userBorrows = borrows[username] ?: emptyList()
            borrowedAdapter.updateData(userBorrows)
        }

        binding.logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun setupRecyclerView(username: String) {
        borrowedAdapter = BorrowedBookAdapter(emptyList()) { returnedBook ->
            showReturnConfirmationDialog(returnedBook, username)
        }
        binding.borrowedBooksRecyclerView.adapter = borrowedAdapter
        binding.borrowedBooksRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun showReturnConfirmationDialog(book: Book, username: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Pengembalian")
            .setMessage("Apakah Anda yakin ingin mengembalikan buku '${book.title}'?")
            .setPositiveButton("Ya") { _, _ ->
                BorrowingManager.returnBook(username, book)
                BookManager.returnBook(book.id)
                Toast.makeText(context, "'${book.title}' telah dikembalikan.", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi Logout")
            .setMessage("Apakah anda yakin ingin log out?")
            .setPositiveButton("Ya") { _, _ -> logout() }
            .setNegativeButton("Tidak", null)
            .show()
    }

    // DIUBAH: Fungsi logout sekarang mengirim "pesan khusus"
    private fun logout() {
        Firebase.auth.signOut()
        val intent = Intent(activity, LoginActivity::class.java)
        // DITAMBAHKAN: Pesan khusus untuk memberitahu LoginActivity agar tidak auto-login
        intent.putExtra("IS_LOGGED_OUT", true)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}