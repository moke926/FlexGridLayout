package com.android.kotlin.flexgridlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.kotlin.flexgridlayout.databinding.ActivityMainBinding
import kotlin.random.Random

data class FlexItem(@DrawableRes val picRes: Int)
data class FlexGroup(val index: Int, val itemList: List<FlexItem>)
class MainActivity : AppCompatActivity() {

    private val mData = MutableLiveData<List<FlexGroup>>()
    init {
        val groupCount = 100
        val list = mutableListOf<FlexGroup>()
        for (i in 0 until groupCount){
            val itemList = mutableListOf<FlexItem>()
            val itemCount = Random.nextInt(1, 11)
            for (j in 0 until itemCount){
                itemList.add(FlexItem(R.mipmap.test001))
            }
            list.add(FlexGroup(i, itemList))
        }

        mData.postValue(list)
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val recyclerView = binding.rvTest
        val adapter = TestAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mData.observe(this, adapter)
    }
}