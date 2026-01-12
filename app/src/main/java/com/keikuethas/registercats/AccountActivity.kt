package com.keikuethas.registercats

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.keikuethas.registercats.databinding.ActivityAccountBinding

class AccountActivity : AppCompatActivity() {

    lateinit var binding: ActivityAccountBinding
    lateinit var viewModel: CatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = CatViewModel(application)
        binding.nameText.setText(viewModel.currentUserName)

        binding.saveButton.setOnClickListener {
            if (binding.nameText.text.toString().isBlank()) makeSnackbar("Name should not be blank")
            else {
                viewModel.updateUserName(binding.nameText.text.toString())
                if (binding.passwordText.text.toString().isNotEmpty())
                    viewModel.updateUserPassword(binding.passwordText.text.toString())
                finish()
            }
        }

        binding.deleteButton.setOnClickListener {
            makeSnackbar("Hold to delete your account. This action cannot be undone")
        }

        binding.deleteButton.setOnLongClickListener {
            viewModel.removeUser()
            finish()
            true
        }
    }

    fun makeSnackbar(message: String) =
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
}