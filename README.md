[![Android Gems](http://www.android-gems.com/badge/DevinShine/MagicCircle.svg?branch=master)](http://www.android-gems.com/lib/DevinShine/MagicCircle)

# [DEPRECATED] MagicCircle
这个项目只是一个练习的作品，用于对代码的理解，所以请不要直接使用到项目中。
A magic circle (｀･ω･´)  ノ

模仿优秀网页设计的[一个效果][1]，也写了对应的[文章][2]。
![pic01.gif-302.1kB](art/pic01.gif)

以后再补充

  [1]: http://weibo.com/1773655610/CzUai6Gid?type=comment#_rnd1442252060746
  [2]: http://www.jianshu.com/p/791d3a791ec2#

2016-12-29 23:49:56
使用(伪)Builder模式，可链式调用对应方法设置动画属性，目前提供以下方法
```java
setInterpolator(Interpolator interpolator);
setRepeatCount(int count);
setRepeatMode(int mode);
setDuration(int duration);
setColor(int color);
//颜色变化，需要两个都设置才有效
setStartColor(int color);
setEndColor(int color);
```