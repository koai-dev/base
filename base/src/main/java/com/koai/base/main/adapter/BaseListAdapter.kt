/*
 * *
 *  * Created by Nguyễn Kim Khánh on 7/18/23, 10:10 AM
 *  * Copyright (c) 2023 . All rights reserved.
 *  * Last modified 7/18/23, 10:10 AM
 *
 */

package com.koai.base.main.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.koai.base.utils.LogUtils

@Suppress("UNCHECKED_CAST")
abstract class BaseListAdapter<DATA : Any, VIEW_BINDING : ViewBinding>(
    private val diffUtil: DiffUtil.ItemCallback<DATA> = TComparator(),
) : BListAdapter<DATA>(diffUtil) {
    abstract fun bindView(
        holder: VH,
        binding: VIEW_BINDING,
        position: Int,
    )

    override fun onBindViewHolder(
        holder: VH,
        position: Int,
    ) {
        super.onBindViewHolder(holder, position)
        try {
            bindView(holder, holder.binding as VIEW_BINDING, holder.bindingAdapterPosition)
        } catch (e: Exception) {
            LogUtils.log("BaseListAdapter", e.message.toString())
        }
    }
}
