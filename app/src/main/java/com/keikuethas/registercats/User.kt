package com.keikuethas.registercats

import android.util.Log
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class User(
    var name: String,
    var login: String,
    var password: String,
    val cats: MutableList<Cat> = mutableListOf(),
    val id: String
) {
    companion object {
        fun create(name: String, login: String, password: String) = User(
            name = name,
            login = login,
            password = password.mess(),
            id = UUID.randomUUID().toString()
        )

        private fun String.mess(): String {
            var res = ""
            for (i in this.indices) {
                val it = this[i]
                res += if (i % 2 == 0) it + i else it - i
            }
            return res.reversed()
        }
    }

    fun comparePassword(password: String) = (password.mess() == this.password)

    fun updatePassword(newPassword: String) {
        password = newPassword.mess()
    }

    private fun String.mess(): String {
        var res = ""
        for (i in this.indices) {
            val it = this[i]
            res += if (i % 2 == 0) it + i else it - i
        }
        return res.reversed()
    }
}
