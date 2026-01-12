package com.keikuethas.registercats

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class CatViewModel(application: Application) : AndroidViewModel(application) {
    private val prefs = application.getSharedPreferences("prefs", MODE_PRIVATE)
    private val users = mutableListOf<User>()

    private var currentUser: User? = null

    val authorized: Boolean
        get() = (currentUser != null)

    val currentUserName: String?
        get() = currentUser?.name

    val currentUserCats: List<Cat>?
        get() = currentUser?.cats?.toList()

    init {
        loadData()
        authorize()
    }

    fun authorize(login: String, password: String, forced: Boolean = false): Boolean {
        if (!forced && authorized) return true
        currentUser =
            users.firstOrNull { user -> (user.login == login) && user.comparePassword(password) }
        prefs.edit {
            putString("login", login)
            putString("password", password)
        }
        Log.i("debug", "currentUser is found: ${currentUser != null}")
        return currentUser != null
    }

    private fun authorize() = if (authorized) true else authorize(
        prefs.getString("login", "")!!,
        prefs.getString("password", "")!!
    )

    fun logOut() {
        currentUser = null
        prefs.edit {
            putString("login", null)
            putString("password", null)
        }
    }

    fun addUser(newUser: User): Boolean {
        users.forEach { user -> if (user.login == newUser.login) return false }
        users.add(newUser)
        saveData()
        return true
    }

//    fun updateUser(updatedUser: User) {
//        val index = users.indexOfFirst { updatedUser.id == it.id }
//        users[index] = updatedUser
//        saveData()
//    }

    fun updateUserName(newName: String) {
        requireAuthorization()

        currentUser!!.name = newName
        saveData()
    }

    fun updateUserPassword(newPassword: String) {
        requireAuthorization()

        currentUser!!.updatePassword(newPassword)
        prefs.edit { putString("password", newPassword) }
        saveData()
    }


    fun addCatForUser(cat: Cat): Boolean {
        requireAuthorization()

        currentUser!!.cats.forEach { if (cat.name == it.name && cat.type == it.type) return false }
        currentUser!!.cats.add(cat)
        Log.i("debug", "current cats: ${Json.encodeToString(currentUser!!.cats)}")
        saveData()
        return true
    }

    fun updateCatForUser(cat: Cat): Boolean {
        requireAuthorization()

        val target = currentUser!!.cats.indexOfFirst { it.id == cat.id }
        if (target == -1) return false
        currentUserCats!!.forEach {
            it -> if ((it.id != cat.id) && (it.name == cat.name) && (it.type == cat.type))
                return false
        }
        currentUser!!.cats[target] = cat
        saveData()
        return true
    }

    fun removeCatForUser(cat: Cat): Boolean {
        requireAuthorization()

        if (currentUser!!.cats.remove(cat)) {
            saveData()
            return true
        } else return false
    }

    fun removeUser(): Boolean =
        if (users.remove(currentUser)) {
            saveData()
            true
        } else false

    private fun loadData() {
        val json = prefs.getString("users", "[]")!!
        val loaded = Json.decodeFromString<List<User>>(json)
        users.addAll(loaded)
    }

    private fun saveData() {
        val json = Json.encodeToString(users)
        prefs.edit {
            putString("users", json)
        }
    }

    private fun requireAuthorization() = require(authorized) { "Authorization needed" }
}