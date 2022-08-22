package com.amandaluz.marvelproject.view.fragment.detail.adapter

import android.view.View
import android.widget.TextView
import com.amandaluz.marvelproject.data.model.modelcomics.Result

class ComicsAndSeriesFields(item: Result) {

    private val page = item.pageCount.toString()
    private val price = item.prices
    private val date = item.dates
    private val itemPrice = item.prices?.first()?.price


    fun setPricesFields(view: TextView, text: String) {
        if (price.isNullOrEmpty() || itemPrice.toString() == "0.0") {
            setVisibilityAs(view, View.INVISIBLE)
        } else {
            setVisibilityAs(view, View.VISIBLE)
            view.text = text
        }
    }

    fun setPageFields(view: TextView, text: String) {
        if (page.isNullOrEmpty() || page == "0") {
            setVisibilityAs(view, View.INVISIBLE)
        } else {
            setVisibilityAs(view, View.VISIBLE)
            view.text = text
        }
    }

    fun setOnSaleDateFields(view: TextView, text: String) {
        if (date.isNullOrEmpty()) {
            setVisibilityAs(view, View.INVISIBLE)
        } else {
            setVisibilityAs(view, View.VISIBLE)
            view.text = text
        }
    }

    fun setVisibilityAs(textView: View, mode: Int) {
        textView.visibility = mode
    }
}