package com.example.mini_project_taman_bacaan

data class User(val username: String, val role: String)

object UserManager {
    private val users = mutableMapOf(
        "admin" to "admin123",
        "user" to "user123"
    )
    private val userRoles = mutableMapOf(
        "admin" to "ADMIN",
        "user" to "USER"
    )

    fun login(username: String, password: String): User? {
        if (users.containsKey(username) && users[username] == password) {
            return User(username, userRoles[username]!!)
        }
        return null
    }

    fun addUser(username: String, password: String, role: String): Boolean {
        if (users.containsKey(username)) return false
        users[username] = password
        userRoles[username] = role
        return true
    }
}