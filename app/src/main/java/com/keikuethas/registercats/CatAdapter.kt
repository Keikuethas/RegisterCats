package com.keikuethas.registercats

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keikuethas.registercats.databinding.ActivityCatItemBinding
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ItemDiffCallback : DiffUtil.ItemCallback<Cat>() { //by Qwen
    override fun areItemsTheSame(oldItem: Cat, newItem: Cat): Boolean {
        return oldItem.id == newItem.id  // ← уникальный идентификатор
    }

    override fun areContentsTheSame(oldItem: Cat, newItem: Cat): Boolean {
        return oldItem == newItem  // ← data class автоматически сравнивает все поля
    }
}
class CatAdapter(val onItemDeleted: (Cat) -> Unit): ListAdapter<Cat, CatAdapter.CatViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CatViewHolder {
        val binding = ActivityCatItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CatViewHolder(binding, onItemDeleted)
    }

    override fun onBindViewHolder(
        holder: CatViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class CatViewHolder(val binding: ActivityCatItemBinding, val onItemDeleted: (Cat) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cat:Cat) {
            binding.cat = cat
            binding.root.setOnClickListener {
                val newIntent = Intent(binding.root.context, CatClickerActivity::class.java)
                val json = Json.encodeToString(binding.cat)
                newIntent.putExtra("cat", json)
                binding.root.context.startActivity(newIntent)
            }
            binding.root.setOnLongClickListener {
                onItemDeleted(binding.cat!!)
                true
            }
            binding.executePendingBindings()
        }
    }
}