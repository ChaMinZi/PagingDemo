package com.example.pagingdemo.utils

import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.pagingdemo.R

@BindingAdapter("imageUrl")
fun bindImageFromUrl(imageView: ImageView, url: String?) {
    url?.let {
        Glide
            .with(imageView.context)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.ic_image)
            .into(imageView)
    }
}

@BindingAdapter("text")
fun TextView.bindDateFormat(string: String?) {
    text = string?.let {
        convertStringToDateString(string)
    }
}