package com.android.kotlin.flexgridlayout

import android.annotation.SuppressLint
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
import com.bumptech.glide.Glide

class TestAdapter: ListAdapter<FlexGroup, TestAdapter.MyViewHolder>(object: ItemCallback<FlexGroup>(){
    override fun areItemsTheSame(oldItem: FlexGroup, newItem: FlexGroup): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: FlexGroup, newItem: FlexGroup): Boolean {
        return oldItem == newItem
    }
}), Observer<List<FlexGroup>> {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val adapter = TestFlexAdapter()
        val groupView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_test_item, null)
        (groupView as? FlexGridGroup)?.apply {
            val list = DefaultLayoutConfiguration.generateGroup(parent.context, viewType)
            addGrids(list)
            setAdapter(adapter)
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
        fun bind(group: FlexGroup){
            adapter.submitData(itemView, group.itemList)
        }
    }

    override fun onChanged(t: List<FlexGroup>?) {
        submitList(t)
    }
}

class TestFlexAdapter: FlexGridGroup.FlexAdapter<FlexItem>() {
    override fun bind(data: FlexItem, flexChild: FlexChild) {
        val view = flexChild as? FlexView
        if(view != null) {
            Glide.with(view.context).load(data.picRes).centerCrop().into(view)
        }
    }
}