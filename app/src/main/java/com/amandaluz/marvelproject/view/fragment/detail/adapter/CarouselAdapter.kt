package com.amandaluz.marvelproject.view.fragment.detail.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.amandaluz.marvelproject.R
import com.amandaluz.marvelproject.data.model.modelcomics.Result
import com.amandaluz.marvelproject.databinding.ItemCarouselBinding
import com.amandaluz.marvelproject.util.Image
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.target.ViewTarget
import kotlin.math.roundToInt

class CarouselAdapter(
    private val itemList: List<Result>,
    private val click: (item: Result) -> Unit
) : RecyclerView.Adapter<CarouselAdapter.MyViewHolder>() {

    private var hasInitParentDimensions = false
    private var maxImageWidth: Int = 0
    private var maxImageHeight: Int = 0
    private var maxImageAspectRatio: Float = 1f

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        setParentDimensions(parent)
        val view = LayoutInflater.from(parent.context)
        val binding = ItemCarouselBinding.inflate(view, parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = itemList[position]

        holder.run {
            layoutParamsConfiguration(category)
            itemView.setOnClickListener {
                val rv = itemView.parent as RecyclerView
                rv.smoothScrollToCenteredPosition(position)
                click.invoke(category)
            }
        }
    }

    override fun getItemCount(): Int = itemList.count()

    class MyViewHolder(
        private val binding: ItemCarouselBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindView(itemCategory: Result) {
            binding.run {
                setCategoryImage(itemCategory)
            }
        }

        private fun ItemCarouselBinding.setCategoryImage(comics: Result): ViewTarget<ImageView, Drawable> {

            var image = "${comics.thumbnail.path}.${comics.thumbnail.extension}"
            val notAvailableDefault =
                "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg"
            val newNotAvailable =
                "https://feb.kuleuven.be/drc/LEER/visiting-scholars-1/image-not-available.jpg"

            return if (image == notAvailableDefault) {
                image = newNotAvailable
                Glide.with(itemView).load(image).transition(withCrossFade()).transform(FitCenter())
                    .into(imageCarousel)
            } else {
                Glide.with(itemView)
                    .load("${comics.thumbnail.path}.${comics.thumbnail.extension}")
                    .transition(withCrossFade()).transform(FitCenter()).into(imageCarousel)
            }
        }
    }

    private fun setParentDimensions(parent: ViewGroup) {
        if (!hasInitParentDimensions) {
            maxImageWidth = (parent.width * 0.75f).roundToInt()
            maxImageHeight = parent.height
            maxImageAspectRatio = maxImageWidth.toFloat() / maxImageHeight.toFloat()
            hasInitParentDimensions = true
        }
    }

    private fun MyViewHolder.scrollToItemClicked(
        position: Int
    ) {
        itemView.setOnClickListener {
            val rv = itemView.parent as RecyclerView
            rv.smoothScrollToCenteredPosition(position)
        }
    }

    private fun RecyclerView.smoothScrollToCenteredPosition(position: Int) {
        val smoothScroller = object : LinearSmoothScroller(context) {
            override fun calculateDxToMakeVisible(view: View?, snapPreference: Int): Int {
                val dxToStart = super.calculateDxToMakeVisible(view, SNAP_TO_START)
                val dxToEnd = super.calculateDxToMakeVisible(view, SNAP_TO_END)

                return (dxToStart + dxToEnd) / 2
            }
        }

        smoothScroller.targetPosition = position
        layoutManager?.startSmoothScroll(smoothScroller)
    }

    private fun MyViewHolder.layoutParamsConfiguration(
        comics: Result
    ) {
        val targetImageWidth: Int = aspectRatioConfiguration(comics)

        itemView.layoutParams =
            RecyclerView.LayoutParams(targetImageWidth, RecyclerView.LayoutParams.MATCH_PARENT)
        bindView(comics)
    }

    private fun aspectRatioConfiguration(comics: Result): Int {
        val imageAspectRatio = comics.thumbnail.aspectRatio

        val targetImageWidth: Int = if (imageAspectRatio < maxImageAspectRatio) {
            (maxImageHeight * imageAspectRatio).roundToInt()
        } else {
            maxImageWidth
        }
        return targetImageWidth
    }

}