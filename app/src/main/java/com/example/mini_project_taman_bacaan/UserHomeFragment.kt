package com.example.mini_project_taman_bacaan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
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
        bookAdapter = BookAdapter(emptyList()) {}
        binding.booksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.booksRecyclerView.adapter = bookAdapter
    }

    private fun showBookDetailDialog(book: Book) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_book_detail, null)
        val builder = AlertDialog.Builder(requireContext()).setView(dialogView).setPositiveButton("Tutup", null)
        val dialog = builder.create()
        dialog.show()

        val cover: ImageView = dialogView.findViewById(R.id.dialogCoverImageView)
        val title: TextView = dialogView.findViewById(R.id.dialogTitleTextView)
        val author: TextView = dialogView.findViewById(R.id.dialogAuthorTextView)
        val publisher: TextView = dialogView.findViewById(R.id.dialogPublisherTextView)
        val description: TextView = dialogView.findViewById(R.id.dialogDescriptionTextView)
        val status: TextView = dialogView.findViewById(R.id.dialogStatusTextView)
        val borrowButton: Button = dialogView.findViewById(R.id.borrowButton)

        title.text = book.title
        author.text = "oleh ${book.author}"
        publisher.text = "${book.publisher} (${book.publicationYear})"
        description.text = book.description

        if (book.stock > 0) {
            status.text = "Tersedia (${book.stock} buah)"
            status.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark))
            borrowButton.isEnabled = true
            borrowButton.text = "Pinjam Buku Ini"
        } else {
            status.text = "Tidak Tersedia"
            status.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark))
            borrowButton.isEnabled = false
            borrowButton.text = "Stok Habis"
        }

        borrowButton.setOnClickListener {
            val username = args.username
            if (username != null) {
                val success = BorrowingManager.borrow(username, book)
                if (success) {
                    BookManager.borrowBook(book.id)
                    Toast.makeText(context, "'${book.title}' berhasil dipinjam!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                } else {
                    Toast.makeText(context, "Anda sudah meminjam buku ini.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        Glide.with(this).load(book.coverUrl).into(cover)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}