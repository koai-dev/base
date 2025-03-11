package com.koai.base.main.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.koai.base.utils.LogUtils

abstract class BasePagingDataAdapter<T : Any, VIEW_BINDING : ViewBinding>(private val diffUtil: DiffUtil.ItemCallback<T> = TComparator()) :
    IPagingDataAdapter<T>(diffUtil) {
    abstract fun bindView(
        holder: VH,
        binding: VIEW_BINDING,
        position: Int,
    )

    override fun onBindViewHolder(
        holder: VH,
        viewType: Int,
    ) {
        super.onBindViewHolder(holder, viewType)
        try {
            bindView(holder, holder.binding as VIEW_BINDING, holder.bindingAdapterPosition)
        } catch (e: Exception) {
            LogUtils.log("BaseListAdapter", e.message.toString())
        }
    }
}
