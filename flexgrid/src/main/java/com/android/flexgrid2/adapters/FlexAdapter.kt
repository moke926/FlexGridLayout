package com.android.flexgrid2.adapters

import android.util.Log
import android.view.View
import com.android.flexgrid2.FlexChild
import com.android.flexgrid2.FlexGridGroup

/**
 * @author moke926
 * @date 2021/12/5
 */
abstract class FlexAdapter<D>{

    private var mList = mutableListOf<D>()
    fun submitData(group: View?, list: List<D>){
        if(group !is FlexGridGroup){
            Log.e(FlexGridGroup.TAG, "groupView with this FlexAdapter is not FlexGridGroup!")
            return
        }
        mList.clear()
        mList.addAll(list)
        group.bindAdapterData()
    }

    fun bindDataAndView(position: Int, flexChild: FlexChild){
        val data = mList.getOrNull(position)
        if(data != null){
            bind(data, flexChild)
        }
    }

    abstract fun bind(data:D, flexChild: FlexChild)

    open fun getItemCount(): Int{
        return mList.size
    }
}