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
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.mini_project_taman_bacaan.databinding.FragmentAdminBooksBinding

class AdminBooksFragment : Fragment() {
    private var _binding: FragmentAdminBooksBinding? = null
    private val binding get() = _binding!!
    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var bookAdapter: BookAdapter
    private val args: AdminBooksFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAdminBooksBinding.inflate(inflater, container, false)
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
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
        binding.addBookButton.setOnClickListener {
            addBook()
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
        dialogView.findViewById<View>(R.id.borrowButton).visibility = View.GONE
        val cover: ImageView = dialogView.findViewById(R.id.dialogCoverImageView)
        val title: TextView = dialogView.findViewById(R.id.dialogTitleTextView)
        val author: TextView = dialogView.findViewById(R.id.dialogAuthorTextView)
        val publisher: TextView = dialogView.findViewById(R.id.dialogPublisherTextView)
        val description: TextView = dialogView.findViewById(R.id.dialogDescriptionTextView)
        val status: TextView = dialogView.findViewById(R.id.dialogStatusTextView)
        title.text = book.title
        author.text = "oleh ${book.author}"
        publisher.text = "${book.publisher} (${book.publicationYear})"
        description.text = book.description
        status.text = "Stok saat ini: ${book.stock}"
        status.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))
        Glide.with(this).load(book.coverUrl).placeholder(R.drawable.loading_animation).error(R.drawable.ic_broken_image).into(cover)
    }

    private fun addBook() {
        val title = binding.addTitleEditText.text.toString().trim()
        val author = binding.addAuthorEditText.text.toString().trim()
        val publisher = binding.addPublisherEditText.text.toString().trim()
        val year = binding.addYearEditText.text.toString().toIntOrNull() ?: 0
        val stock = binding.addStockEditText.text.toString().toIntOrNull() ?: 0
        val coverUrl = binding.addCoverUrlEditText.text.toString().trim()
        val description = binding.addDescEditText.text.toString().trim()

        if (title.isNotEmpty() && author.isNotEmpty()) {
            val newBook = Book(title = title, description = description, coverUrl = coverUrl, author = author, publisher = publisher, publicationYear = year, stock = stock)
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
        binding.addStockEditText.text.clear()
        binding.addCoverUrlEditText.text.clear()
        binding.addDescEditText.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}