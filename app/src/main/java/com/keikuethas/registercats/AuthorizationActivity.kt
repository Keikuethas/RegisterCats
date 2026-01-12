package com.keikuethas.registercats

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.keikuethas.registercats.databinding.ActivityAuthorizationBinding

class AuthorizationActivity : AppCompatActivity() {

    lateinit var binding: ActivityAuthorizationBinding
    lateinit var viewModel: CatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = CatViewModel(application)
        if (viewModel.authorized) finish()

        binding.registerText.setOnClickListener {
            val newIntent = Intent(this, RegistrationActivity::class.java)
            startActivity(newIntent)
        }

        binding.loginButton.setOnClickListener {
            val login = binding.loginText.text.toString()
            val password = binding.passwordText.text.toString()
            if (login.isBlank()) makeSnackbar("Login should not be blank")
            else if (!viewModel.authorize(login, password))
                makeSnackbar("Incorrect password or login")
            else finish()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel = CatViewModel(application)
        if (viewModel.authorized) finish()
    }

    fun makeSnackbar(message:String) =
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
}