package com.example.mini_project_taman_bacaan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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

    // Setup View Binding untuk fragment ini
    private var _binding: FragmentAdminBooksBinding? = null
    private val binding get() = _binding!!

    // Inisialisasi ViewModel dan Adapter
    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var bookAdapter: BookAdapter

    // Mengambil argumen (username) yang dikirim dari MainActivity
    private val args: AdminBooksFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Menghubungkan layout XML (fragment_admin_books.xml) ke file Kotlin ini
        _binding = FragmentAdminBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Menampilkan pesan selamat datang dengan username yang diterima
        val username = args.username
        binding.welcomeTextView.text = "Selamat Datang, $username!"

        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    // Mengatur RecyclerView untuk pertama kali
    private fun setupRecyclerView() {
        bookAdapter = BookAdapter(emptyList()) { selectedBook ->
            showBookDetailDialog(selectedBook)
        }
        binding.booksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.booksRecyclerView.adapter = bookAdapter
    }

    // Mengatur semua pengamatan (observe) ke LiveData dari ViewModel
    private fun setupObservers() {
        // Mengamati perubahan pada daftar buku
        bookViewModel.books.observe(viewLifecycleOwner) { bookList ->
            // Membuat ulang adapter dengan data baru setiap kali daftar buku diperbarui
            bookAdapter = BookAdapter(bookList) { selectedBook ->
                showBookDetailDialog(selectedBook)
            }
            binding.booksRecyclerView.adapter = bookAdapter
        }

        // Mengamati status loading
        bookViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Mengamati pesan error
        bookViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    // Mengatur listener untuk tombol-tombol
    private fun setupClickListeners() {
        binding.addBookButton.setOnClickListener {
            addBook()
        }
    }

    // Fungsi untuk menampilkan pop-up detail buku (versi Admin)
    private fun showBookDetailDialog(book: Book) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_book_detail, null)
        val mainDialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()

        // Ambil semua view dari layout dialog
        val cover: ImageView = dialogView.findViewById(R.id.dialogCoverImageView)
        val title: TextView = dialogView.findViewById(R.id.dialogTitleTextView)
        val author: TextView = dialogView.findViewById(R.id.dialogAuthorTextView)
        val publisher: TextView = dialogView.findViewById(R.id.dialogPublisherTextView)
        val description: TextView = dialogView.findViewById(R.id.dialogDescriptionTextView)
        val status: TextView = dialogView.findViewById(R.id.dialogStatusTextView)
        val updateStockButton: Button = dialogView.findViewById(R.id.updateStockButton)

        // Sembunyikan tombol pinjam yang hanya untuk user
        dialogView.findViewById<View>(R.id.borrowButton).visibility = View.GONE

        // Tampilkan tombol ubah stok yang hanya untuk admin
        updateStockButton.visibility = View.VISIBLE

        // Isi semua data buku ke dalam view
        title.text = book.title
        author.text = "oleh ${book.author}"
        publisher.text = "${book.publisher} (${book.publicationYear})"
        description.text = book.description
        status.text = "Stok Saat Ini: ${book.stock}"
        status.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.black))

        Glide.with(this).load(book.coverUrl).into(cover)

        // Atur listener untuk tombol ubah stok
        updateStockButton.setOnClickListener {
            mainDialog.dismiss() // Tutup dialog detail
            showUpdateStockDialog(book) // Buka dialog baru untuk mengubah stok
        }

        mainDialog.show()
    }

    // Fungsi untuk menampilkan pop-up kedua (khusus untuk input stok)
    private fun showUpdateStockDialog(book: Book) {
        val updateDialogView = LayoutInflater.from(context).inflate(R.layout.dialog_update_stock, null)
        val newStockEditText: EditText = updateDialogView.findViewById(R.id.newStockEditText)
        newStockEditText.setText(book.stock.toString()) // Isi dengan stok saat ini

        AlertDialog.Builder(requireContext())
            .setTitle("Ubah Stok untuk '${book.title}'")
            .setView(updateDialogView)
            .setPositiveButton("OK") { _, _ ->
                val newStockString = newStockEditText.text.toString()
                val newStock = newStockString.toIntOrNull()

                // Validasi: pastikan input adalah angka dan tidak negatif
                if (newStock != null && newStock >= 0) {
                    BookManager.setStock(book.id, newStock)
                    Toast.makeText(context, "Stok berhasil diperbarui menjadi $newStock", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Jumlah stok tidak valid", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    // Fungsi untuk menambah buku baru
    private fun addBook() {
        val title = binding.addTitleEditText.text.toString().trim()
        val author = binding.addAuthorEditText.text.toString().trim()
        val publisher = binding.addPublisherEditText.text.toString().trim()
        val year = binding.addYearEditText.text.toString().toIntOrNull() ?: 0
        val stock = binding.addStockEditText.text.toString().toIntOrNull() ?: 0
        val coverUrl = binding.addCoverUrlEditText.text.toString().trim()
        val description = binding.addDescEditText.text.toString().trim()

        if (title.isNotEmpty() && author.isNotEmpty()) {
            val newBook = Book(
                title = title,
                description = description,
                coverUrl = coverUrl,
                author = author,
                publisher = publisher,
                publicationYear = year,
                stock = stock
            )
            BookManager.addBook(newBook)
            Toast.makeText(context, "Buku berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            clearForm()
        } else {
            Toast.makeText(context, "Judul dan Penulis tidak boleh kosong", Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi untuk membersihkan form setelah berhasil menambah buku
    private fun clearForm() {
        binding.addTitleEditText.text.clear()
        binding.addAuthorEditText.text.clear()
        binding.addPublisherEditText.text.clear()
        binding.addYearEditText.text.clear()
        binding.addStockEditText.text.clear()
        binding.addCoverUrlEditText.text.clear()
        binding.addDescEditText.text.clear()
    }

    // Penting untuk membersihkan binding saat fragment dihancurkan
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}