package com.android.kotlin.flexgridlayout

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.flexgrid2.FlexChild
import com.android.flexgrid2.FlexGridGroup
import com.android.flexgrid2.FlexView
import com.android.flexgrid2.GridConfig.DefaultLayoutConfiguration
import com.android.flexgrid2.adapters.FlexAdapter
import com.android.flexgrid2.listeners.OnItemClickListener
import com.android.flexgrid2.listeners.OnItemLongClickListener
import com.bumptech.glide.Glide

class TestAdapter: ListAdapter<FlexGroup, TestAdapter.MyViewHolder>(object: ItemCallback<FlexGroup>(){
    override fun areItemsTheSame(oldItem: FlexGroup, newItem: FlexGroup): Boolean {
        return oldItem.index == newItem.index
    }

    override fun areContentsTheSame(oldItem: FlexGroup, newItem: FlexGroup): Boolean {
        return oldItem.itemList.size == newItem.itemList.size
    }
}), Observer<List<FlexGroup>> {

    companion object{
        const val TAG = "TestAdapter"
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val adapter = TestFlexAdapter()
        val groupView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_test_item, null)
        (groupView as? FlexGridGroup)?.apply {
            // generate the default layout frame,
            // this step will decide the approximate appearance of this flexgridgroup
            val list = DefaultLayoutConfiguration.generateGroup(parent.context, viewType)
            addGrids(list)
            setAdapter(adapter)
            isLongClickable = true
        }
        return MyViewHolder(groupView, adapter)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyViewHolder(groupView: View, private val adapter: TestFlexAdapter): RecyclerView.ViewHolder(groupView){
        private val mItemClickListener = object : OnItemClickListener{
            override fun onChildClick(index: Int, child: FlexChild) {
                Log.i(TAG,"onChildClick index=$index, child=$child")
            }
        }
        private val mItemLongClickListener = object : OnItemLongClickListener{
            override fun onChildLongClick(index: Int, child: FlexChild) {
                Log.i(TAG,"onChildLongClick index=$index, child=$child")
            }

        }
        fun bind(group: FlexGroup){
            adapter.submitData(itemView, group.itemList)
            (itemView as? FlexGridGroup)?.addOnItemLongClickListener(mItemClickListener)
            (itemView as? FlexGridGroup)?.addOnItemLongClickListener(mItemLongClickListener)
        }
    }

    override fun onChanged(t: List<FlexGroup>?) {
        val list = mutableListOf<FlexGroup>()
        t?.forEach {
            val itemList = it.itemList.toList()
            list.add(FlexGroup(it.index, itemList))
        }
        submitList(list)
    }
}

class TestFlexAdapter: FlexAdapter<FlexItem>() {
    override fun bind(data: FlexItem, flexChild: FlexChild) {
        val view = flexChild as? FlexView
        if(view != null) {
            Glide.with(view.context).load(data.picRes).centerCrop().into(view)
        }
    }
}