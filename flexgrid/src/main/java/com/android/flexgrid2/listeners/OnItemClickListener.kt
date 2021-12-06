package com.android.flexgrid2.listeners

import com.android.flexgrid2.FlexChild

/**
 * @author moke926
 * @date 2021/12/4
 */
interface OnItemClickListener {
    fun onChildClick(index: Int, child: FlexChild)
}

interface OnItemLongClickListener {
    fun onChildLongClick(index: Int, child: FlexChild)
}