# FlexGridLayout
支持显示多宫格的列表布局（以九宫格为基础）

## 目的：
为了通过多宫格显示图片来体现不对称之美，类似IOS相册那样，我设计了一个比较简易的框架，能够支持开发者自定义每组图片的布局。

## 效果图：
<div align=center>
<img src="https://github.com/moke926/media-resources/blob/main/images/Screenshot_1638278611.png" width=30%/>
</div>

<div align=center>
<img src="https://github.com/moke926/media-resources/blob/main/images/Screenshot_1638279091.png" width=30%/>
</div>

<div align=center>
<img src="https://github.com/moke926/media-resources/blob/main/images/Screenshot_1638279116.png" width=30%/>
</div>

<div align=center>
<img src="https://github.com/moke926/media-resources/blob/main/images/Screenshot_1638279124.png" width=30%/>
</div>

## 导入
Gradle:
```gradle
implementation 'io.github.moke926:flexgrid:1.0.0'
```

## 怎么使用
每一个多宫格布局都是由多个小矩形拼接而成。

### FlexChild

你可以自定义这些小矩形，只要你实现 `FlexGrid` 这个接口就行， 比如内部默认已经写好的 `FlexView` :
```kotlin
class FlexView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatImageView(context, attrs, defStyleAttr), FlexChild {

    private var mStartCoordinate = Pair(0, 0)
    private var mEndCoordinate = Pair(0, 0)

    override fun setCoordinate(startCoordinate: Pair<Int, Int>, endCoordinate: Pair<Int, Int>) {
        mStartCoordinate = startCoordinate
        mEndCoordinate = endCoordinate
    }
    ...
}
```
其中 `setCoordinate(...)`这个方法必须实现，用来传入代表该小矩形的左上角坐标和右下角坐标，这两个坐标在一起可以描述这个小矩形一组多宫格布局中的位置和大小。这两个坐标并不是真实的`view`的坐标值，而是一组虚拟坐标，如下图所示：

<div align=center>
<img src="https://github.com/moke926/media-resources/blob/main/images/coordinateSample.png" width=30%/>
</div>

`FlexGridGroup`内部就可以通过对每个实现了`FlexGrid`接口的对象读取他们的左上角坐标和右下角坐标来进行测量和布局。

### FlexGridGroup
`FlexGridGroup`组件扮演着容纳一组排列好的小矩形的容器角色，通过读取其中所有小矩形的虚拟坐标来绘制出一个多宫格布局。

