package com.amandaluz.marvelproject.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amandaluz.marvelproject.data.model.Results
import com.amandaluz.marvelproject.databinding.CharacterItemBinding
import com.bumptech.glide.Glide

class CharacterAdapter(
    private val characterList: List<Results>,
    private val itemClick: ((item: Results) -> Unit),
    private val longClick: ((item: Results) -> Unit)? = null
) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = CharacterItemBinding.inflate(view, parent, false)
        return CharacterViewHolder(binding, itemClick, longClick)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bindView(characterList[position])
    }

    override fun getItemCount(): Int = characterList.size

    class CharacterViewHolder(
        private val binding: CharacterItemBinding,
        private val itemClick: (item: Results) -> Unit,
        private val longClick: ((item: Results) -> Unit)? = null
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(character: Results) {
            binding.run {
                txtNameCharacterItem.text = character.name

                Glide.with(itemView)
                    .load("${character.thumbnail.path}.${character.thumbnail.extension}")
                    .centerCrop()
                    .into(imgCharacter)

                itemView.setOnClickListener {
                    itemClick.invoke(character)
                }

                itemView.setOnLongClickListener {
                    longClick?.invoke(character)
                    return@setOnLongClickListener true
                }
            }
        }
    }
}