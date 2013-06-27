# Android Wheel Menu

This is a simple android library to make a circular menu UI. Just specify a couple of params and it's ready to use!

# Usage

First [download the jar](https://www.dropbox.com/s/vwftt66loxugq13/wheelmenu.jar) and add it to your project. The exact location to include the jar will depend on your specific IDE. You can also add [wheel-menu-library](https://github.com/anupcowkur/Android-Wheel-Menu/tree/master/wheel-menu-library) as a [library project](https://developer.android.com/tools/projects/projects-eclipse.html#ReferencingLibraryProject) and reference it directly without adding the jar.

Import the package ```com.anupcowkur.wheelmenu``` into the activity in which you want to include the menu.

Initialize the menu with the constructor in the format:

```
new WheelMenu(Context, no_of_divisions_in_your_menu, 
                       drawable_to_use_as_wheel_img, 
                       imageview_for_the_wheel, 
                       snap_to_center_flag);
```

**ex: A menu with 12 divisions** 

![-- Sorry, Image unavailable :-(  ](http://i.imgur.com/1k65UUv.png)


Initialize the menu:

```
WheelMenu wm = new WheelMenu(MainActivity.this, 
                             12, R.drawable.wheel, 
                             wheel_image, true);
```

wheel_image is the id of the image view

```
ImageView wheel_image = (ImageView) findViewById(R.id.wheel);
```

and in the xml file:

```
<ImageView
        android:src="@drawable/wheel"
        android:id="@+id/wheel"
        android:scaleType="matrix"
        android:layout_height="300dp"
        android:layout_width="300dp"></ImageView>
```

That's it!

Now, to get the currently selected position simply call
```
wm.getSelected();
```

**This will return values in the range 1....no_of_divisions_in_your_menu**

In our example, it will return values from 1 to 12 depending on the user's selection.

####snap-to-center

This flag is set to true by default. If true, crossing into a division will cause the wheel to auto-align itself to the center of the division. Otherwise, the wheel will just hang around wherever the user left it. 

Check out the [sample application](https://github.com/anupcowkur/Android-Wheel-Menu/tree/master/wheel-menu-sample) to see it in action!

# Contributing
  Contributions welcome via Github pull requests.
 
# License
 This project is licensed under the MIT License. Please refer the [License.txt](https://github.com/anupcowkur/Android-Wheel-Menu/blob/master/License.txt) file.
