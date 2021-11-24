package com.android.flexgrid2.GridConfig

import android.content.Context
import com.android.flexgrid2.FlexChild
import com.android.flexgrid2.FlexView

/**
 * @author moke926
 * @date 2021/11/21
 */
object DefaultLayoutConfiguration {

    fun generateGroup(context: Context, size: Int): List<FlexChild>{
        if(size <= 0){ return emptyList()}
        return when(size){
            1 -> getOneGrid(context)
            2 -> getTwoGrid(context)
            3 -> getThreeGrid(context)
            4 -> getFourGrid(context)
            5 -> getFiveGrid(context)
            6 -> getSixGrid(context)
            7 -> getSevenGrid(context)
            8 -> getEightGrid(context)
            9 -> getNineGrid(context)
            10 -> { getTenGrid(context) }
            else -> {
                getTenGrid(context)
            }
        }
    }

    fun getOneGrid(context: Context): List<FlexChild>{
        return listOf(FlexView(context).apply {
            setCoordinate(Pair(0,0), Pair(2,2))
        })
    }

    fun getTwoGrid(context: Context): List<FlexChild>{
        val list = mutableListOf<FlexChild>()
        list.add(FlexView(context).apply {
            setCoordinate(Pair(0,0), Pair(2,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(3,0), Pair(5,2))
        })
        return list
    }

    fun getThreeGrid(context: Context): List<FlexChild>{
        val list = mutableListOf<FlexChild>()
        list.add(FlexView(context).apply {
            setCoordinate(Pair(0,0), Pair(2,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(3,0), Pair(3,1))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(3,2), Pair(3,2))
        })
        return list
    }

    fun getFourGrid(context: Context): List<FlexChild>{
        val list = mutableListOf<FlexChild>()
        list.add(FlexView(context).apply {
            setCoordinate(Pair(0,0), Pair(2,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(3,0), Pair(4,1))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(3,2), Pair(3,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(4,2), Pair(4,2))
        })
        return list
    }

    fun getFiveGrid(context: Context): List<FlexChild>{
        val list = mutableListOf<FlexChild>()
        list.add(FlexView(context).apply {
            setCoordinate(Pair(0,0), Pair(2,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(3,0), Pair(3,1))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(3,2), Pair(3,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(4,0), Pair(5,1))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(4,2), Pair(5,2))
        })
        return list
    }

    fun getSixGrid(context: Context): List<FlexChild>{
        val list = mutableListOf<FlexChild>()
        list.add(FlexView(context).apply {
            setCoordinate(Pair(0,0), Pair(2,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(3,0), Pair(4,1))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(3,2), Pair(3,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(4,2), Pair(4,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(5,0), Pair(5,1))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(5,2), Pair(5,2))
        })
        return list
    }

    fun getSevenGrid(context: Context): List<FlexChild>{
        val list = mutableListOf<FlexChild>()
        list.add(FlexView(context).apply {
            setCoordinate(Pair(0,0), Pair(2,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(3,0), Pair(4,1))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(3,2), Pair(3,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(4,2), Pair(4,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(5,0), Pair(5,0))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(5,1), Pair(5,1))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(5,2), Pair(5,2))
        })
        return list
    }

    fun getEightGrid(context: Context): List<FlexChild>{
        val list = mutableListOf<FlexChild>()
        list.add(FlexView(context).apply {
            setCoordinate(Pair(0,0), Pair(2,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(3,0), Pair(4,1))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(3,2), Pair(3,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(4,2), Pair(5,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(5,0), Pair(5,1))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(6,0), Pair(6,0))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(6,1), Pair(6,1))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(6,2), Pair(6,2))
        })
        return list
    }

    fun getNineGrid(context: Context): List<FlexChild>{
        val list = mutableListOf<FlexChild>()
        list.add(FlexView(context).apply {
            setCoordinate(Pair(0,0), Pair(2,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(3,0), Pair(3,0))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(3,1), Pair(3,1))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(3,2), Pair(3,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(4,0), Pair(5,1))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(4,2), Pair(5,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(6,0), Pair(6,0))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(6,1), Pair(6,1))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(6,2), Pair(6,2))
        })
        return list
    }

    fun getTenGrid(context: Context): List<FlexChild>{
        val list = mutableListOf<FlexChild>()
        list.add(FlexView(context).apply {
            setCoordinate(Pair(0,0), Pair(2,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(3,0), Pair(3,0))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(3,1), Pair(3,1))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(3,2), Pair(3,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(4,0), Pair(5,1))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(4,2), Pair(4,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(5,2), Pair(5,2))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(6,0), Pair(6,0))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(6,1), Pair(6,1))
        })
        list.add(FlexView(context).apply {
            setCoordinate(Pair(6,2), Pair(6,2))
        })
        return list
    }
}