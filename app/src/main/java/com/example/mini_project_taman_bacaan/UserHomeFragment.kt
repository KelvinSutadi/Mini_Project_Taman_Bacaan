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
import com.example.mini_project_taman_bacaan.databinding.FragmentUserHomeBinding

class UserHomeFragment : Fragment() {

    private var _binding: FragmentUserHomeBinding? = null
    private val binding get() = _binding!!

    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var bookAdapter: BookAdapter

    // Mengambil argumen dengan aman
    private val args: UserHomeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DITAMBAHKAN: Tampilkan pesan selamat datang
        val username = args.username
        binding.welcomeTextView.text = "Selamat Datang, $username!"

        setupRecyclerView()

        bookViewModel.books.observe(viewLifecycleOwner) { bookList ->
            bookAdapter = BookAdapter(bookList)
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