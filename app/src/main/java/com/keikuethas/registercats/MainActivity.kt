package com.keikuethas.registercats

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.keikuethas.registercats.databinding.ActivityMainBinding
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: CatViewModel
    lateinit var adapter: CatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        register()
        binding.nameText.text = viewModel.currentUserName

        adapter = CatAdapter { cat ->
            viewModel.removeCatForUser(cat)
            adapter.submitList(viewModel.currentUserCats)
        }
        adapter.submitList(viewModel.currentUserCats)
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this)

        binding.catType.keyListener = null
        binding.catType.setAdapter(
            ArrayAdapter(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                Cat.types
            )
        )
        var selectedType: Int? = null
        binding.catType.setOnItemClickListener { _, _, position, _ ->
            selectedType = position
            // Опционально: можно скрыть клавиатуру/выпадашку
            binding.catType.dismissDropDown()
        }


        binding.addButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            if (name.isBlank()) makeSnackbar("Cat name should not be blank")
            else if (selectedType == null) makeSnackbar("Select cat type")
            else if (!viewModel.addCatForUser(
                    Cat(
                        name,
                        type = selectedType
                    )
                )
            ) makeSnackbar("Such cat already exists")
            else
            {
                val cats = viewModel.currentUserCats!!.toList()
                (binding.recycler.adapter as CatAdapter).submitList(cats)
                binding.nameEditText.text.clear()
                binding.catType.text.clear()
            }
        }

        binding.logOutButton.setOnClickListener {
            viewModel.logOut()
            register()
        }

        binding.manageButton.setOnClickListener {
            val newIntent = Intent(this, AccountActivity::class.java)
            startActivity(newIntent)
        }

    }

    override fun onResume() {
        super.onResume()
        register()
        adapter.submitList(viewModel.currentUserCats?.toList())
    }

    fun register() {
        viewModel = CatViewModel(application)
        //Log.i("debug", "Is authorized:${viewModel.authorized}")
        if (!viewModel.authorized) {
            val newIntent = Intent(this, AuthorizationActivity::class.java)
            startActivity(newIntent)
        } else
        {
            binding.nameText.text = viewModel.currentUserName
        }
    }

    fun makeSnackbar(message: String) =
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
}