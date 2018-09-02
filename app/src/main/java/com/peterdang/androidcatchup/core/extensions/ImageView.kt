package com.peterdang.androidcatchup.core.extensions

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("android:imageUri")
fun ImageView.setImageUrl(uri: Uri?) {
    Picasso.get().load(uri).into(this)
}