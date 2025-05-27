/*
 * *
 *  * Created by Nguyễn Kim Khánh on 7/18/23, 10:10 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 7/18/23, 10:10 AM
 *
 */

package com.koai.base.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.koai.base.main.extension.safeClick

@SuppressLint("DiffUtilEquals")
class TComparator<DATA : Any> : DiffUtil.ItemCallback<DATA>() {
    override fun areItemsTheSame(
        oldItem: DATA,
        newItem: DATA,
    ): Boolean = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: DATA,
        newItem: DATA,
    ): Boolean = oldItem == newItem
}

abstract class BListAdapter<DATA : Any>(
    private val diffUtil: DiffUtil.ItemCallback<DATA> = TComparator(),
) : ListAdapter<DATA, BListAdapter.VH>(diffUtil) {
    var listener: Action<DATA>? = null
    var observer: Observer<DATA>? = null

    class VH(
        val binding: ViewBinding,
    ) : ViewHolder(binding.root)

    abstract fun getLayoutId(viewType: Int): Int

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): VH =
        VH(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                getLayoutId(viewType),
                parent,
                false,
            ),
        )

    override fun onBindViewHolder(
        holder: VH,
        position: Int,
    ) {
        holder.binding.root.safeClick {
            listener?.click(holder.bindingAdapterPosition, getItem(holder.bindingAdapterPosition))
        }
    }

    interface Observer<T> {
        /**
         * @param root is viewItem (for visible handle)
         * @param childView can be child recycle_view or viewpager
         * @param data of item
         * @param code for handle
         */
        fun subData(
            root: View,
            childView: View,
            data: T,
            code: Int = 0,
        )
    }
}

interface Action<T> {
    /**
     * @param position of viewItem
     * @param data of viewItem
     */
    fun click(
        position: Int,
        data: T,
        code: Int = 0,
    )
}
