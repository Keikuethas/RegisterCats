package com.keikuethas.registercats

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.keikuethas.registercats.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistrationBinding
    lateinit var viewModel: CatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = CatViewModel(application)

        binding.loginText.setOnClickListener { finish() }

        binding.registerButton.setOnClickListener {
            val name = binding.nameText.text.toString()
            val login = binding.loginEditText.text.toString()
            val password = binding.passwordText.text.toString()
            val password2 = binding.confirmPasswordText.text.toString()
            if (name.isBlank()) makeSnackbar("Name should not be blank")
            else if (login.isBlank()) makeSnackbar("Login should not be blank")
            else if (password.isBlank()) makeSnackbar("Password should not be blank")
            else if (password != password2) makeSnackbar("Passwords don't match")
            else if (viewModel.addUser(User.create(name, login, password))) finish()
            else makeSnackbar("User with such login already exists")
        }
    }

    fun makeSnackbar(message:String) =
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
}