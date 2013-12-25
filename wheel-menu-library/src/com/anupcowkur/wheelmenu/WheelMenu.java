package com.anupcowkur.wheelmenu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class WheelMenu extends ImageView {

    private Bitmap imageOriginal, imageScaled;     //variables for original and re-sized image
    private Matrix matrix;                         //Matrix used to perform rotations
    private int wheelHeight, wheelWidth;           //height and width of the view
    private int top;                               //the current top of the wheel (calculated in wheel divs)
    private double totalRotation;                  //variable that counts the total rotation during a given rotation of the wheel by the user (from ACTION_DOWN to ACTION_UP)
    private int divCount;                          //no of divisions in the wheel
    private int divAngle;                          //angle of each division
    private int selectedPosition;                  //the section currently selected by the user.
    private boolean snapToCenterFlag = true;       //variable that determines whether to snap the wheel to the center of a div or not
    private int angleOffset;					   //how offset from the top the position of "selected" is
    private Context context;
    private WheelChangeListener wheelChangeListener;

    public WheelMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    //initializations
    private void init(Context context) {
        this.context = context;
        this.setScaleType(ScaleType.MATRIX);
        selectedPosition = 0;

        // initialize the matrix only once
        if (matrix == null) {
            matrix = new Matrix();
        } else {
            matrix.reset();
        }

        //touch events listener
        this.setOnTouchListener(new WheelTouchListener());
    }

    /**
     * Add a new listener to observe user selection changes.
     *
     * @param wheelChangeListener
     */
    public void setWheelChangeListener(WheelChangeListener wheelChangeListener) {
        this.wheelChangeListener = wheelChangeListener;
    }

    /**
     * Returns the position currently selected by the user.
     *
     * @return the currently selected position between 1 and divCount.
     */
    public int getSelectedPosition() {
        return selectedPosition;
    }

    /**
     * Set no of divisions in the wheel menu.
     *
     * @param divCount no of divisions.
     */
    public void setDivCount(int divCount) {
        this.divCount = divCount;

        divAngle = 360 / divCount;
        totalRotation = -1 * (divAngle / 2);
    }

    /**
     * Set the snap to center flag. If true, wheel will always snap to center of current section.
     *
     * @param snapToCenterFlag
     */
    public void setSnapToCenterFlag(boolean snapToCenterFlag) {
        this.snapToCenterFlag = snapToCenterFlag;
    }
    
    /**
     * Set how offset from the center the position of the selected div is.
     * Should be set after setDivCount
     * 
     * @param angleOffset
     */
    public void setAngleOffset(int angleOffset){
    	this.angleOffset = angleOffset;
    	top += Math.floor(angleOffset / divAngle);
    	
    	selectedPosition = top;
    }

    /**
     * Set the wheel image.
     *
     * @param drawableId the id of the drawable to be used as the wheel image.
     */
    public void setWheelImage(int drawableId) {
        imageOriginal = BitmapFactory.decodeResource(context.getResources(), drawableId);
    }

    /*
     * We need this to get the dimensions of the view. Once we get those, We can scale the image to make sure it's proper,
     * Initialize the matrix and align it with the views center.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // method called multiple times but initialized just once
        if (wheelHeight == 0 || wheelWidth == 0) {
            wheelHeight = h;
            wheelWidth = w;
            // resize the image
            Matrix resize = new Matrix();
            resize.postScale((float) Math.min(w, h) / (float) imageOriginal.getWidth(), (float) Math.min(wheelWidth, wheelHeight) / (float) imageOriginal.getHeight());
            imageScaled = Bitmap.createBitmap(imageOriginal, 0, 0, imageOriginal.getWidth(), imageOriginal.getHeight(), resize, false);
            // translate the matrix to the image view's center
            float translateX = wheelWidth / 2 - imageScaled.getWidth() / 2;
            float translateY = wheelHeight / 2 - imageScaled.getHeight() / 2;
            matrix.postTranslate(translateX, translateY);
            WheelMenu.this.setImageBitmap(imageScaled);
            WheelMenu.this.setImageMatrix(matrix);
        }
    }

    /**
     * get the angle of a touch event.
     */
    private double getAngle(double x, double y) {
        x = x - (wheelWidth / 2d);
        y = wheelHeight - y - (wheelHeight / 2d);

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

    /**
     * get the quadrant of the wheel which contains the touch point (x,y)
     *
     * @return quadrant 1,2,3 or 4
     */
    private static int getQuadrant(double x, double y) {
        if (x >= 0) {
            return y >= 0 ? 1 : 4;
        } else {
            return y >= 0 ? 2 : 3;
        }
    }

    /**
     * rotate the wheel by the given angle
     *
     * @param degrees
     */
    private void rotateWheel(float degrees) {
        matrix.postRotate(degrees, wheelWidth / 2, wheelHeight / 2);
        WheelMenu.this.setImageMatrix(matrix);

        //add the rotation to the total rotation
        totalRotation = totalRotation + degrees;
    }

    /**
     * Interface to to observe user selection changes.
     */
    public interface WheelChangeListener {
        /**
         * Called when user selects a new position in the wheel menu.
         *
         * @param selectedPosition the new position selected.
         */
        public void onSelectionChange(int selectedPosition);
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
                    totalRotation = totalRotation % 360;

                    //represent total rotation in positive value
                    if (totalRotation < 0) {
                        totalRotation = 360 + totalRotation;
                    }

                    //calculate the no of divs the rotation has crossed
                    int no_of_divs_crossed = (int) ((totalRotation) / divAngle);
                    
                    //calculate current top
                    top = (divCount + top - no_of_divs_crossed) % divCount;

                    //for next rotation, the initial total rotation will be the no of degrees inside the current top
                    totalRotation = totalRotation % divAngle;

                    //snapping to the top's center
                    if (snapToCenterFlag) {

                        //calculate the angle to be rotated to reach the top's center.
                        double leftover = divAngle / 2 - totalRotation;

                        rotateWheel((float) (leftover));

                        //re-initialize total rotation
                        totalRotation = divAngle / 2;
                    }

                    //set the currently selected option
                    if (top == 0) {
                        selectedPosition = divCount-1;//loop around the array
                    } else {
                        selectedPosition = top-1;
                    }

                    if (wheelChangeListener != null) {
                        wheelChangeListener.onSelectionChange(selectedPosition);
                    }

                    break;
            }

            return true;
        }
    }

}
