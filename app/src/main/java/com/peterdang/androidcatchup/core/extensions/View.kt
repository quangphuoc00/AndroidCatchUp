package com.peterdang.androidcatchup.core.extensions

import android.view.View

fun View.isVisible() = this.visibility == View.VISIBLE

fun View.invisible() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}