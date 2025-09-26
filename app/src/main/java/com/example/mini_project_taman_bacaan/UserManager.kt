package com.example.mini_project_taman_bacaan

// Kelas data sederhana untuk merepresentasikan seorang pengguna
data class User(
    val username: String,
    val password: String,
    val role: String
)

// Singleton object ini bertindak sebagai database sementara kita
object UserManager {

    // Daftar untuk menyimpan username dan password
    private val users = mutableMapOf(
        "admin" to "admin123", // Data awal untuk admin
        "user" to "user123"    // Data awal untuk user
    )

    // Daftar untuk menyimpan peran (role) dari setiap username
    private val userRoles = mutableMapOf(
        "admin" to "ADMIN",
        "user" to "USER"
    )

    /**
     * Fungsi untuk memeriksa kredensial login.
     * Mengembalikan objek User jika berhasil, atau null jika gagal.
     */
    fun login(username: String, password: String): User? {
        if (users.containsKey(username) && users[username] == password) {
            // Jika username ada dan password cocok, kembalikan data user
            return User(username, password, userRoles[username]!!)
        }
        // Jika tidak, kembalikan null (login gagal)
        return null
    }

    /**
     * Fungsi untuk menambahkan pengguna baru (digunakan oleh admin).
     * Mengembalikan true jika berhasil, atau false jika username sudah ada.
     */
    fun addUser(username: String, password: String, role: String): Boolean {
        if (users.containsKey(username)) {
            return false // Gagal, karena username sudah terdaftar
        }
        users[username] = password
        userRoles[username] = role
        return true // Berhasil menambahkan user baru
    }
}
