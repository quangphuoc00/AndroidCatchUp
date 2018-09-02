package com.peterdang.androidcatchup.core.blueprints

interface ItemRecyclerViewClick<T> {
    fun onItemClick(item: T)
}