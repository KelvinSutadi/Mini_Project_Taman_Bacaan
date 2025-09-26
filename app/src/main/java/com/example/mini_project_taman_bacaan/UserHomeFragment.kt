package com.example.mini_project_taman_bacaan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mini_project_taman_bacaan.databinding.FragmentUserHomeBinding

class UserHomeFragment : Fragment() {

    private var _binding: FragmentUserHomeBinding? = null
    private val binding get() = _binding!!

    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var bookAdapter: BookAdapter
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

        val username = args.username
        binding.welcomeTextView.text = "Selamat Datang, $username!"

        setupRecyclerView()

        bookViewModel.books.observe(viewLifecycleOwner) { bookList ->
            // Saat membuat adapter, kita juga definisikan apa yang terjadi saat item diklik
            bookAdapter = BookAdapter(bookList) { selectedBook ->
                showBookDetailDialog(selectedBook)
            }
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
        // Inisialisasi adapter dengan daftar kosong dan listener kosong
        bookAdapter = BookAdapter(emptyList()) {}
        binding.booksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.booksRecyclerView.adapter = bookAdapter
    }

    // DITAMBAHKAN: Fungsi untuk menampilkan dialog detail buku
    private fun showBookDetailDialog(book: Book) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_book_detail, null)
        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Tutup", null)

        val dialog = builder.create()
        dialog.show()

        // Ambil view dari dialog dan isi datanya
        val cover: ImageView = dialogView.findViewById(R.id.dialogCoverImageView)
        val title: TextView = dialogView.findViewById(R.id.dialogTitleTextView)
        val author: TextView = dialogView.findViewById(R.id.dialogAuthorTextView)
        val publisher: TextView = dialogView.findViewById(R.id.dialogPublisherTextView)
        val description: TextView = dialogView.findViewById(R.id.dialogDescriptionTextView)

        title.text = book.title
        author.text = "oleh ${book.author}"
        publisher.text = "${book.publisher} (${book.publicationYear})"
        description.text = book.description

        Glide.with(this)
            .load(book.coverUrl)
            .into(cover)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}