package com.shubham.movie_mania_upgrade.ui.adapter

import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView

@BindingAdapter("loadImage")
fun bindingImageToUrl(view: ShapeableImageView, url: String) {
    Glide.with(view.context)
        .load(url)
        .into(view)
}