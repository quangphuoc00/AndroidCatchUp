package com.peterdang.androidcatchup.core.blueprints

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Kotlin: items?.size?:0.  [?.] if left == null return null. Otherwise, return left
 *                          [?:] if left == null return right. Otherwise, return left
 */
abstract class BaseRecyclerAdapter<T> : RecyclerView.Adapter<MyViewHolder<T>>() {

    private var items: List<T>? = null

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MyViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
                layoutInflater, viewType, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder<T>,
                                  position: Int) {
        val obj = getObjForPosition(position)
        holder.bind(obj)
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }

    protected fun getObjForPosition(position: Int): T? = items?.get(position)

    protected abstract fun getLayoutIdForPosition(position: Int): Int

    override fun getItemCount(): Int = items?.size ?: 0

    fun setData(newItems: List<T>?) {
        items = newItems
        notifyDataSetChanged()
    }
}