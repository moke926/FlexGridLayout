package com.android.kotlin.flexgridlayout

import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.flexgrid2.FlexChild
import com.android.flexgrid2.FlexGridGroup
import com.android.flexgrid2.GridConfig.DefaultLayoutConfiguration
import com.android.flexgrid2.OnDataBindCallBack

class TestAdapter: ListAdapter<FlexGroup, TestAdapter.MyViewHolder>(object: ItemCallback<FlexGroup>(){
    override fun areItemsTheSame(oldItem: FlexGroup, newItem: FlexGroup): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: FlexGroup, newItem: FlexGroup): Boolean {
        return oldItem == newItem
    }
}), Observer<List<FlexGroup>> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val flexGroupView = FlexGridGroup<FlexItem>(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            val list = DefaultLayoutConfiguration.generateGroup(parent.context, viewType)
            addGrids(list)
            addCallBack(DataCallBack())
        }
        return MyViewHolder(flexGroupView)
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemList.size
    }

    override fun onViewRecycled(holder: MyViewHolder) {
        holder.groupView.recycle()
        super.onViewRecycled(holder)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyViewHolder(val groupView: FlexGridGroup<FlexItem>): RecyclerView.ViewHolder(groupView){
        fun bind(group: FlexGroup){
            groupView.submitData(group.itemList)
        }
    }

    class DataCallBack: OnDataBindCallBack<FlexItem> {
        override fun onBind(data: FlexItem, child: FlexChild) {
            (child as? ImageView)?.setImageResource(data.picRes)
        }

    }

    override fun onChanged(t: List<FlexGroup>?) {
        submitList(t)
    }
}