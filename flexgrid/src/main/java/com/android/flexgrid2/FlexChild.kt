package com.android.flexgrid2

interface FlexChild {

    fun setCoordinate(startCoordinate: Pair<Int, Int>, endCoordinate: Pair<Int, Int>)

    fun getStartCoordinate(): Pair<Int, Int>

    fun getEndCoordinate(): Pair<Int, Int>

    fun getWidthUnits() = getEndCoordinate().second - getStartCoordinate().second + 1

    fun getHeightUnits() = getEndCoordinate().first - getStartCoordinate().first + 1
}