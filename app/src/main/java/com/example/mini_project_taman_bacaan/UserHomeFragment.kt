package com.example.mini_project_taman_bacaan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mini_project_taman_bacaan.databinding.FragmentUserHomeBinding // Nama binding sudah benar

class UserHomeFragment : Fragment() {

    private var _binding: FragmentUserHomeBinding? = null // Nama binding sudah benar
    private val binding get() = _binding!!

    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var bookAdapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserHomeBinding.inflate(inflater, container, false) // Nama binding sudah benar
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        bookViewModel.books.observe(viewLifecycleOwner) { books ->
            bookAdapter = BookAdapter(books)
            binding.booksRecyclerView.adapter = bookAdapter
        }

        bookViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        bookViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
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