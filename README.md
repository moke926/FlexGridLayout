# FlexGridLayout
自定义的多宫格布局

### 目的：
为了通过多宫格显示图片来体现不对称之美，类似IOS相册那样，我设计了一个比较简易的框架，能够支持开发者自定义每组图片的布局。

### 效果图：
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

### 导入
Gradle:
```gradle
implementation 'io.github.moke926:flexgrid:1.0.0'
```

### 怎么使用
每一组多宫格布局都是由多个小矩形拼接而成。

#### [FlexChild](https://github.com/moke926/FlexGridLayout/blob/main/flexgrid/src/main/java/com/android/flexgrid2/FlexChild.kt)

小矩形是这个组件的最小单位View，你可以自定义这些小矩形，只要你实现 `FlexGrid` 这个接口就行， 比如内部默认已经写好的 `FlexView` :
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
其中 `setCoordinate(...)`这个方法必须实现，用来传入代表该小矩形的左上角坐标和右下角坐标，这两个坐标在一起可以描述这个小矩形一组多宫格布局中的位置和大小。这两个坐标并不是真实的`view`的坐标值，而是一组虚拟坐标，举个例子，如下图所示：

<div align=center>
<img src="https://github.com/moke926/media-resources/blob/main/images/coordinateSample.png" width=30%/>
</div>

`FlexGridGroup`内部就可以通过对每个实现了`FlexGrid`接口的对象读取他们的左上角坐标和右下角坐标来进行测量和布局。


#### [FlexGridGrop](https://github.com/moke926/FlexGridLayout/blob/main/flexgrid/src/main/java/com/android/flexgrid2/FlexGridGroup.kt)
`FlexGridGroup`组件扮演着容纳一组排列好的小矩形的容器角色即ViewGroup，通过读取其中包含的小矩形虚拟坐标来绘制出一组多宫格布局。
你在实例化`FlexGridGroup`的时候需要调用`addGrids(list: List<FlexChild>)`和`setAdapter(adapter: FlexAdapter<*>)`。

**addGrid**：
你需要传入一组实现了FlexChild接口的View对象用来描述这一个多宫格布局的基本框架， 而这些对象请确定都已经设定好了左上角坐标和右下角坐标，当然你也可以传入非View，不过我会在内部根据坐标转成默认的`FlexView`对象。

**setAdapter**：
`FlexGridLayout`采取了适配器模式，你需要自定义一个adapter继承`FlexViewGroup.FlexAdapter`，并指定Adapter的泛型为你的数据类型，如`sample`用例中的`TestFlexAdapter`：
```kotlin
class TestFlexAdapter: FlexGridGroup.FlexAdapter<FlexItem>() {
    override fun bind(data: FlexItem, flexChild: FlexChild) {
        val view = flexChild as? FlexView
        if(view != null) {
            ...
        }
    }
}
```
你可以通过重写的`bind(...)`方法中获得一个数据和对应位置的 `FlexChild` 对象，之后你就可以为所欲为了。

`FlexGridGroup` 拥有自定义属性，目前的属性如下：
```
<declare-styleable name="FlexGridGroup">
        <attr name="horizontalGap" format="dimension"/>
        <attr name="verticalGap" format="dimension"/>
        <attr name="standUnitSize" format="dimension"/>
</declare-styleable>
```
**horizontalGap** :
左右两个小矩形水平方向上的间隔

**verticalGap**：
上下两个小矩形竖直方向上的间隔

**standUnitSize**：
特别情况下，你可以指定这个值来设置单位矩形大小。比如， 左上角坐标为 ( 0 , 0 )且右下角坐标为 ( 0 , 1 )的矩形，包含了2个单位矩形， 左上角坐标为 ( 0 , 0 )且右下角坐标为 ( 2 , 2 )的矩形，包含了9个单位矩形，这是最基本的九宫格结构。

当然，具体的使用方法请参考用例模块 `example`。

### 未来开发计划：

目前下阶段的任务是：
*    item变化的动画

欢迎朋友一起来讨论和完善此框架。

