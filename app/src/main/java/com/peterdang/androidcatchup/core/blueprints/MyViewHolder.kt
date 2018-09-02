package com.peterdang.androidcatchup.core.blueprints

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.peterdang.androidcatchup.BR


class MyViewHolder<T>(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(obj: T?) {
        if (obj == null)
            return
        binding.setVariable(BR.row, obj)
        binding.executePendingBindings()
    }
}