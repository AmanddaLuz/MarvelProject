package com.amandaluz.marvelproject.util

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ViewFlipper
import com.amandaluz.marvelproject.data.model.modelcomics.Result
import com.amandaluz.marvelproject.databinding.ComicDetailBinding
import com.amandaluz.marvelproject.view.fragment.detail.adapter.CarouselAdapter
import com.bumptech.glide.Glide

class CustomCategory(context: Context, attrs: AttributeSet?) : ViewFlipper(context, attrs) {
    private var list = arrayListOf<Result>()
    private lateinit var carouselAdapter: CarouselAdapter

    fun setList(bodyList: ArrayList<Result>) {
        this.list = bodyList
    }

    fun getListSize(): Int {
        return this.list.size
    }

    fun getItemBody(position: Int) = list[position]

    fun setLayout() {
        list.forEach { result ->
            result.run {
                val binding = ComicDetailBinding.inflate(
                    LayoutInflater.from(context),
                    this@CustomCategory,
                    false
                )
                binding.run {
                    Glide.with(context)
                        .load("${result.thumbnail.path}.${result.thumbnail.extension}")
                        .into(imgDetail)
                    txtTitleDetails.text = result.title
                    txtDescriptionDetails.text = result.description
                }
            }

        }
    }
}