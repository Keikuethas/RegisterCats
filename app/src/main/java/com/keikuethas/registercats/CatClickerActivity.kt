package com.keikuethas.registercats

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.keikuethas.registercats.databinding.ActivityCatClickerBinding
import kotlinx.serialization.json.Json

class CatClickerActivity : AppCompatActivity() {

    lateinit var binding: ActivityCatClickerBinding
    lateinit var viewModel: CatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCatClickerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel = CatViewModel(application)

        val json = intent.getStringExtra("cat")
        if (json == null) finish()
        else {
            val cat = Json.decodeFromString<Cat>(json)
            binding.cat = cat
        }

        binding.catImage.setImageResource(binding.cat!!.imageType)

        binding.catImage.setOnClickListener {
            binding.cat!!.clicks++
            binding.counter.text = binding.cat!!.clicks.toString()
            viewModel.updateCatForUser(binding.cat!!)
        }

        binding.renameButton.setOnClickListener {
            if (binding.renameButton.text == "Rename") {
                binding.renameButton.text = "Save"
                binding.nameText.visibility = GONE
                binding.nameEditText.visibility = VISIBLE
                binding.nameEditText.setText(binding.cat!!.name)
            }
            else {
                val oldName = binding.cat!!.name
                binding.cat!!.name = binding.nameEditText.text.toString()
                binding.nameText.text = binding.cat!!.name
                if (viewModel.updateCatForUser(binding.cat!!)) {
                    binding.renameButton.text = "Rename"
                    binding.nameText.visibility = VISIBLE
                    binding.nameEditText.visibility = GONE
                } else {
                    binding.cat!!.name = oldName
                    makeSnackbar("Such cat already exists")
                }
            }
        }

        binding.returnButton.setOnClickListener {
            binding.cat!!.favouriteFood = binding.foodText.text.toString()
            binding.cat!!.tailLength = binding.tailText.text.toString().toInt()
            viewModel.updateCatForUser(binding.cat!!)
            finish()
        }
    }

    fun makeSnackbar(message: String) =
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
}