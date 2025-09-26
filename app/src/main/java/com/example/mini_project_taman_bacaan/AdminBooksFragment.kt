package com.example.mini_project_taman_bacaan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mini_project_taman_bacaan.databinding.FragmentAdminBooksBinding

class AdminBooksFragment : Fragment() {

    private var _binding: FragmentAdminBooksBinding? = null
    private val binding get() = _binding!!

    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var bookAdapter: BookAdapter

    // Menggunakan AdminBooksFragmentArgs yang dibuat oleh Safe Args
    private val args: AdminBooksFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAdminBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tampilkan pesan selamat datang untuk admin
        val username = args.username
        binding.welcomeTextView.text = "Selamat Datang, $username!"

        setupRecyclerView()

        // Amati daftar buku dan perbarui RecyclerView
        bookViewModel.books.observe(viewLifecycleOwner) { bookList ->
            bookAdapter = BookAdapter(bookList)
            binding.booksRecyclerView.adapter = bookAdapter
        }

        bookViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        bookViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        // Atur listener untuk tombol tambah buku
        binding.addBookButton.setOnClickListener {
            addBook()
        }
    }

    private fun setupRecyclerView() {
        bookAdapter = BookAdapter(emptyList())
        binding.booksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.booksRecyclerView.adapter = bookAdapter
    }

    private fun addBook() {
        val title = binding.addTitleEditText.text.toString().trim()
        val author = binding.addAuthorEditText.text.toString().trim()
        val publisher = binding.addPublisherEditText.text.toString().trim()
        val year = binding.addYearEditText.text.toString().toIntOrNull() ?: 0
        val coverUrl = binding.addCoverUrlEditText.text.toString().trim()
        val description = binding.addDescEditText.text.toString().trim()

        if (title.isNotEmpty() && author.isNotEmpty()) {
            val newBook = Book(title, description, coverUrl, author, publisher, year)
            BookManager.addBook(newBook)
            Toast.makeText(context, "Buku berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            clearForm()
        } else {
            Toast.makeText(context, "Judul dan Penulis tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearForm() {
        binding.addTitleEditText.text.clear()
        binding.addAuthorEditText.text.clear()
        binding.addPublisherEditText.text.clear()
        binding.addYearEditText.text.clear()
        binding.addCoverUrlEditText.text.clear()
        binding.addDescEditText.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}