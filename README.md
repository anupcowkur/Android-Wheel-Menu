# Android Wheel Menu

This is a simple android library to make a circular menu UI. Just specify a couple of params and it's ready to use!

![Example Image](https://raw.github.com/anupcowkur/Android-Wheel-Menu/master/graphics/wheel.gif)

# Usage

First [download the jar](https://www.dropbox.com/s/6qsib5ktc1th9gq/wheel-menu-library.jar) and add it to your project. The exact instructions to include the jar will depend on your specific development environment, IDE etc. You can also add [wheel-menu-library](https://github.com/anupcowkur/Android-Wheel-Menu/tree/master/wheel-menu-library) as a [library project](https://developer.android.com/tools/projects/index.html#LibraryProjects) and reference it directly without adding the jar.


Include wheel menu in your layout:

```
<com.anupcowkur.wheelmenu.WheelMenu
android:id="@+id/wheelMenu"
android:layout_width="300dp"
android:layout_height="300dp" />
```

Initialize the wheel menu in your activity:

```
wheelMenu = (WheelMenu) findViewById(R.id.wheelMenu);

//set the no of divisions in the wheel, default is 1
wheelMenu.setDivCount(12);

//set the drawable to be used as the wheel image. If you
//don't set this, you'll get a  NullPointerException.
wheelMenu.setWheelImage(R.drawable.wheel);
```

That's it!

Now, to get the currently selected position simply call:

```
wheelMenu.getSelectedPosition();
```

or, you can set a listener to monitor change events like so:

```
wheelMenu.setWheelChangeListener(new WheelMenu.WheelChangeListener() {
@Override
public void onSelectionChange(int selectedPosition) {
      //do your thing
    }
});
```

####snap-to-center

This flag is set to true by default. If true, crossing into a division will cause the wheel to auto-align itself to the center of the division. Otherwise, the wheel will just hang around wherever the user left it. You can change it by calling:

```
wheelMenu.setSnapToCenterFlag(false)
```

Check out the [sample application](https://github.com/anupcowkur/Android-Wheel-Menu/tree/master/wheel-menu-sample) to see it in action!

# Contributing
Contributions welcome via Github pull requests.

# License
This project is licensed under the MIT License. Please refer the [License.txt](https://github.com/anupcowkur/Android-Wheel-Menu/blob/master/License.txt) file.
