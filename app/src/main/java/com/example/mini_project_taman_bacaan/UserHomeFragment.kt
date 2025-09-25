package com.example.mini_project_taman_bacaan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mini_project_taman_bacaan.databinding.FragmentUserHomeBinding // Nama yang benar

class UserHomeFragment : Fragment() {

    // Pola standar untuk View Binding di Fragment
    private var _binding: FragmentUserHomeBinding? = null // Nama yang benar
    private val binding get() = _binding!!

    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var bookAdapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Menggunakan nama kelas binding yang benar
        _binding = FragmentUserHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        // Mengamati perubahan pada daftar buku
        bookViewModel.books.observe(viewLifecycleOwner) { bookList ->
            bookAdapter = BookAdapter(bookList)
            binding.booksRecyclerView.adapter = bookAdapter
        }
    }

    private fun setupRecyclerView() {
        bookAdapter = BookAdapter(emptyList())
        binding.booksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.booksRecyclerView.adapter = bookAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}