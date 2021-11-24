package com.android.flexgrid2.GridConfig

import androidx.annotation.IntDef

/**
 * @author moke926
 * @date 2021/11/21
 */
@IntDef(GridType.ONE, GridType.TWO, GridType.THREE, GridType.FOUR, GridType.FIVE, GridType.SIX,
    GridType.SEVEN, GridType.EIGHT, GridType.NINE, GridType.TEN)
@Retention(AnnotationRetention.SOURCE)
annotation class GridType{
    companion object{
        const val ONE = 1
        const val TWO = 2
        const val THREE = 3
        const val FOUR = 4
        const val FIVE = 5
        const val SIX = 6
        const val SEVEN = 7
        const val EIGHT = 8
        const val NINE = 9
        const val TEN = 10
    }
}