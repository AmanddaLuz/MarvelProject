package com.amandaluz.marvelproject.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amandaluz.marvelproject.data.model.Results
import com.amandaluz.marvelproject.databinding.CharacterItemBinding
import com.bumptech.glide.Glide

class CharacterAdapter(private val characterList: MutableList<Results>) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = CharacterItemBinding.inflate(view, parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bindView(characterList[position])
    }

    override fun getItemCount(): Int = characterList.size

    class CharacterViewHolder(private val binding: CharacterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(character: Results) {
            binding.run {
                txtNameCharacterItem.text = character.name
                val portrait = "/portrait_medium."
                Glide.with(itemView)
                    .asBitmap()
                    .load("${character.thumbnail.path}${portrait}${character.thumbnail.extension}")
                    .centerCrop()
                    .into(imgCharacter)
            }
        }
    }
}