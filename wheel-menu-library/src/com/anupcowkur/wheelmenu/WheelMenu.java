package com.anupcowkur.wheelmenu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class WheelMenu {

    private Bitmap imageOriginal, imageScaled;     //variables for original and resized image
    private Context context;                       //variable to store the context of the calling activity
    private Matrix matrix;                         //Matrix used to perform rotations
    private ImageView wheel;                       //image view which holds the wheel
    private int wheelHeight, wheelWidth;           //height and width of the image view containing the wheel
    private int top;                               //the current top of the wheel
    private double total_rotation;                 //variable that counts the total rotation during a given rotation of the wheel by the user (from ACTION_DOWN to ACTION_UP)
    private int divcount;                          //variable that counts the no of divisons in the wheel
    private int divangle;                          //variable that holds the angle of each division
    private int selected;                          //the currently selected option by the user
    private boolean snapFlag = true;               //variable that determines whether to snap the wheel to the center of a div or not


    //public method to be used to retrieve the currently selected position
    public int getSelected() {

        return selected;
    }


    /* constructor form:
    *  WheelMenu(Context, no_of_divisions_in_the_menu, drawable_to_use_as_wheel_img, imageview_for_the_wheel, flag_to_set_snap_to_center_option)
    */
    public WheelMenu(Context context_par, int divcount_par, int drawable_id, ImageView wheel_par, boolean snapFlag_par) {

        //initializations
        selected = 1;
        divcount = divcount_par;
        context = context_par;
        snapFlag = snapFlag_par;
        wheel = wheel_par;
        top = 0;
        divangle = 360 / divcount;
        total_rotation = -1 * (divangle / 2);

        // load the image only once
        if (imageOriginal == null) {
            imageOriginal = BitmapFactory.decodeResource(context.getResources(), drawable_id);
        }
        // initialize the matrix only once
        if (matrix == null) {
            matrix = new Matrix();
        } else {
            matrix.reset();
        }

        //touch events listener
        wheel.setOnTouchListener(new WheelTouchListener());


        /* listener to find out when layout is initialized. We need this to get the dimensions of the image view.
        *  Once we get those, We can scale the image to make sure it's proper, Initialize the matrix and align it with the image view's center.
        */
        wheel.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // method called multiple times but we can initialize just once
                if (wheelHeight == 0 || wheelWidth == 0) {
                    wheelHeight = wheel.getHeight();
                    wheelWidth = wheel.getWidth();
                    // resize the image
                    Matrix resize = new Matrix();
                    resize.postScale((float) Math.min(wheelWidth, wheelHeight) / (float) imageOriginal.getWidth(), (float) Math.min(wheelWidth, wheelHeight) / (float) imageOriginal.getHeight());
                    imageScaled = Bitmap.createBitmap(imageOriginal, 0, 0, imageOriginal.getWidth(), imageOriginal.getHeight(), resize, false);
                    // translate the matrix to the image view's center
                    float translateX = wheelWidth / 2 - imageScaled.getWidth() / 2;
                    float translateY = wheelHeight / 2 - imageScaled.getHeight() / 2;
                    matrix.postTranslate(translateX, translateY);
                    wheel.setImageBitmap(imageScaled);
                    wheel.setImageMatrix(matrix);
                }
            }
        });


    }


    //listener for touch events on the wheel
    private class WheelTouchListener implements View.OnTouchListener {
        private double startAngle;


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    //get the start angle for the current move event
                    startAngle = getAngle(event.getX(), event.getY());
                    break;


                case MotionEvent.ACTION_MOVE:
                    //get the current angle for the current move event
                    double currentAngle = getAngle(event.getX(), event.getY());

                    //rotate the wheel by the difference
                    rotateWheel((float) (startAngle - currentAngle));

                    //current angle becomes start angle for the next motion
                    startAngle = currentAngle;
                    break;


                case MotionEvent.ACTION_UP:
                    //get the total angle rotated in 360 degrees
                    total_rotation = total_rotation % 360;

                    //represent total rotation in positive value
                    if (total_rotation < 0) {
                        total_rotation = 360 + total_rotation;
                    }

                    //calculate the no of divs the rotation has crossed
                    int no_of_divs_crossed = (int) (total_rotation / divangle);

                    //calculate current top
                    top = (divcount + top - no_of_divs_crossed) % divcount;

                    //for next rotation, the initial total rotation will be the no of degrees inside the current top
                    total_rotation = total_rotation % divangle;

                    //snapping to the top's center
                    if (snapFlag) {

                        //calculate the angle to be rotated to reach the top's center.
                        double leftover = divangle / 2 - total_rotation;

                        rotateWheel((float) (leftover));

                        //re-initiliaze total rotation
                        total_rotation = divangle / 2;
                    }

                    //set the currently selected option
                    if (top == 0) {
                        selected = divcount;
                    } else {
                        selected = top;
                    }

                    break;
            }

            return true;
        }
    }

    //get the angle of a touch event
    private double getAngle(double xTouch, double yTouch) {
        double x = xTouch - (wheelWidth / 2d);
        double y = wheelHeight - yTouch - (wheelHeight / 2d);

        switch (getQuadrant(x, y)) {
            case 1:
                return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 2:
                return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 3:
                return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
            case 4:
                return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            default:
                return 0;
        }
    }

    //get the quadrant of the wheel which the user has touched
    private static int getQuadrant(double x, double y) {
        if (x >= 0) {
            return y >= 0 ? 1 : 4;
        } else {
            return y >= 0 ? 2 : 3;
        }
    }


    //rotate the wheel by the given angle
    private void rotateWheel(float degrees) {
        matrix.postRotate(degrees, wheelWidth / 2, wheelHeight / 2);
        wheel.setImageMatrix(matrix);

        //add the rotation to the total rotation
        total_rotation = total_rotation + degrees;


    }

}