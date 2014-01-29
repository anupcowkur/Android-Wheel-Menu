# Android Wheel Menu

![Example Image](https://raw.github.com/anupcowkur/Android-Wheel-Menu/master/graphics/wheel.gif)

# Usage

First [download the jar](https://github.com/anupcowkur/Android-Wheel-Menu/releases/download/v1.0/wheel-menu-library.jar) and add it to your project. The exact instructions to include the jar will depend on your specific development environment, IDE etc. You can also add [wheel-menu-library](https://github.com/anupcowkur/Android-Wheel-Menu/tree/master/wheel-menu-library) as a [library project](https://developer.android.com/tools/projects/index.html#LibraryProjects) and reference it directly without adding the jar.


Include wheel menu in your layout:

```xml
<com.anupcowkur.wheelmenu.WheelMenu
android:id="@+id/wheelMenu"
android:layout_width="300dp"
android:layout_height="300dp" />
```

Initialize the wheel menu in your activity:

```java
wheelMenu = (WheelMenu) findViewById(R.id.wheelMenu);

//set the no of divisions in the wheel, default is 1
wheelMenu.setDivCount(12);

//set the drawable to be used as the wheel image. If you
//don't set this, you'll get a  NullPointerException.
wheelMenu.setWheelImage(R.drawable.wheel);
```

That's it!

Now, to get the currently selected position simply call:

```java
wheelMenu.getSelectedPosition();
```

or, you can set a listener to monitor change events like so:

```java
wheelMenu.setWheelChangeListener(new WheelMenu.WheelChangeListener() {
@Override
public void onSelectionChange(int selectedPosition) {
      //do your thing
    }
});
```

wheel positions will start from 0.

####snap-to-center

This flag is set to true by default. If true, crossing into a division will cause the wheel to auto-align itself to the center of the division. Otherwise, the wheel will just hang around wherever the user left it. You can change it by calling:

```java
wheelMenu.setSnapToCenterFlag(false)
```

####Changing the top position

You can also make any arbitrary div in the wheel to be the "top" postion by calling

```java
wheelMenu.setAlternateTopDiv(int);
```

For instance, if you call ```wheelMenu.setAlternateTopDiv(6)``` then the sixth div will now be considered as the "top" and all calculations from that point on will reflect this. This can be useful in situations such as when you want the bottom div or right div of the wheel to be the selected position.

A caveat with this is that you have to call this method after the call to ```setDivCount(int)``` . If you call it before, then your new top position will be ignored and the top will still be 0.

# Sample

Check out the [sample application](https://github.com/anupcowkur/Android-Wheel-Menu/tree/master/wheel-menu-sample) to see it in action!

# Contributing
Contributions welcome via Github pull requests.

# License
This project is licensed under the MIT License. Please refer the [License.txt](https://github.com/anupcowkur/Android-Wheel-Menu/blob/master/License.txt) file.
