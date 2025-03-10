package com.koai.base.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.koai.base.main.extension.safeClick

abstract class IPagingDataAdapter<T : Any>(private val diffUtil: DiffUtil.ItemCallback<T> = TComparator()) :
    PagingDataAdapter<T, IPagingDataAdapter.VH>(diffUtil) {
    var listener: Action<T>? = null

    class VH(val binding: ViewBinding) : ViewHolder(binding.root)

    override fun onBindViewHolder(
        holder: VH,
        viewType: Int,
    ) {
        holder.binding.root.safeClick {
            getItem(holder.bindingAdapterPosition)?.let { item ->
                listener?.click(
                    holder.bindingAdapterPosition,
                    item,
                )
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = VH(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            getLayoutId(viewType),
            parent,
            false,
        ),
    )

    abstract fun getLayoutId(viewType: Int): Int
}
