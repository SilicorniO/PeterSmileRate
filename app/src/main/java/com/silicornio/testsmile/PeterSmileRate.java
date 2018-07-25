/**
 * Created by Javier S.R. @ Silicornio on 25 of july of 2018
 * MIT License
 * @author  Javier S.R.
 * @version 1.0
 * @since   2018-07-25
 */

package com.silicornio.testsmile;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PeterSmileRate extends View {

    //SIZES
    private static final float EYE_SIZE = 0.3f;
    private static final float EYE_HAPPINESS = 0.5f;
    private static final float MOUTH_SIZE = 0.5f;
    private static final float HAPPINESS_LIMIT = 0.15f;

    //COLORS
    private static final int[] colors = new int[] {
            Color.parseColor("#E23135"), Color.parseColor("#E4D942"), Color.parseColor("#3CE4b8")
    };

    //variables
    private int mStrokeWidth = 30;

    /** Paint for lines **/
    private Paint mLinePaint;

    //Paths to paint
    private Path mPathCircle;
    private Path mPathEyeLeft;
    private Path mPathEyeRight;
    private Path mPathMouth;

    //Size of the view
    private int mWidth = 0;
    private int mHeight = 0;

    //positions of the eyes and mouth depending of size
    private int mEyesY = 0;
    private int mMouthY = 0;
    private int mEyesXL0 = 0; //eye left X0
    private int mEyesXLF = 0; //eye left XF
    private int mEyesXR0 = 0; //eye right X0
    private int mEyesXRF = 0; //eye right XF
    private int mMouthX0 = 0; //mouth X0
    private int mMouthXF = 0; //mouth XF

    //color calculations
    private int mColorHappinessSize = 0;

    /** Flag to know if we have done calculations **/
    private boolean mCalculated = false;

    //values to calculate speed and happiness
    private int mHappinessLimit = 0;
    private float mHappinessSpeed = 0f;

    /** Smile difference counter to show happy or sad **/
    private int mHappiness = 0;

    //Touch events calculations
    private int mLastY = 0;

    /** Listener to send events **/
    private PeterSmileRatingListener mListener;

    public PeterSmileRate(Context context) {
        super(context);

        //initialize
        init(null);
    }

    public PeterSmileRate(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //initialize
        init(attrs);
    }

    public PeterSmileRate(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //initialize
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        //attributes
        if (attrs != null) {
            //initialize attributes if necessary
        }

        //initialize paints
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(mStrokeWidth);
        mLinePaint.setColor(Color.GREEN);         // set the size
        mLinePaint.setDither(true);                    // set the dither to true
        mLinePaint.setStyle(Paint.Style.STROKE);       // set to STOKE
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //save the size
        mWidth = w;
        mHeight = h;

        //start to calculate with these sizes
        calculate();
    }

    // ----- SET / GET -----

    /**
     * Set the listener to receive events
     * @param listener PeterSmileRating
     */
    public void setListener(PeterSmileRatingListener listener) {
        mListener = listener;
    }

    // ----- CALCULATIONS -----

    private void calculate() {

        //check we have a width and a height
        if (mWidth == 0 ||mHeight == 0) {
            return;
        }

        //rest of calculations
        sizeCalculations();
        calculateMouthAndEyes();
        calculateColor();

        //calculations made
        mCalculated = true;

        //force redraw
        this.invalidate();
    }

    private void sizeCalculations() {
        //calculate happiness limit and speed
        mHappinessLimit = (int) (mHeight * HAPPINESS_LIMIT);
        mHappinessSpeed = 0.5f;

        //calculate the space for each color
        mColorHappinessSize = (mHappinessLimit * 2) / colors.length;

    }

    private void calculateMouthAndEyes() {

        //calculate the position of eyes and mouth
        mEyesY = (mHeight * 40) / 100;
        mMouthY = (mHeight * 65) / 100;

        int eyeSize = (int)((mWidth / 2) * EYE_SIZE);
        int mouthSize = (int)(mWidth * MOUTH_SIZE);

        mEyesXL0 = ((mWidth / 3) - (eyeSize / 2));
        mEyesXLF = mEyesXL0 + eyeSize;
        mEyesXR0 = (((mWidth * 2) / 3) - (eyeSize / 2));
        mEyesXRF = mEyesXR0 + eyeSize;
        mMouthX0 = (mWidth - mouthSize) / 2;
        mMouthXF = mMouthX0 + mouthSize;

        //calculate eye happiness
        int eyeHappiness = (int) (mHappiness * EYE_HAPPINESS);
        if (eyeHappiness > 0) {
            eyeHappiness = -eyeHappiness;
        } else if (eyeHappiness < 0) {
            eyeHappiness = 0;
        }

        //calculate mouth happiness
        int mouthHappiness = mHappiness;
//        if (mouthHappiness < 0 && mouthHappiness > -mStrokeWidth / 2) {
//            mouthHappiness = -mStrokeWidth / 2;
//        } else if (mouthHappiness > 0 && mouthHappiness < mStrokeWidth / 2) {
//            mouthHappiness = mStrokeWidth / 2;
//        }

        //calculate circle
        mPathCircle = new Path();
        mPathCircle.moveTo(mWidth / 2, 0);
        mPathCircle.addArc(new RectF(mStrokeWidth, mStrokeWidth, mWidth - mStrokeWidth, mHeight - mStrokeWidth), -90, 180);
        mPathCircle.addArc(new RectF(mStrokeWidth, mStrokeWidth, mWidth - mStrokeWidth, mHeight - mStrokeWidth), 180, 360);

        //calculate mouth
        mPathMouth = new Path();
        mPathMouth.moveTo(mMouthX0, mMouthY);
        if (mouthHappiness > mStrokeWidth / 2) {
            mPathMouth.addArc(new RectF(mMouthX0, mMouthY - mouthHappiness, mMouthXF, mMouthY + mouthHappiness), 0, 180);
        } else if (mouthHappiness < -mStrokeWidth / 2) {
            mPathMouth.addArc(new RectF(mMouthX0, mMouthY - Math.abs(mouthHappiness), mMouthXF, mMouthY + Math.abs(mouthHappiness)), 0, -180);
        } else {
            mPathMouth.lineTo(mMouthXF, mMouthY);
        }

        //calculate eyes
        mPathEyeLeft = new Path();
        mPathEyeLeft.moveTo(mEyesXL0, mEyesY);
        if (eyeHappiness < -mStrokeWidth / 2) {
            mPathEyeLeft.addArc(new RectF(mEyesXL0, mEyesY - Math.abs(eyeHappiness), mEyesXLF, mEyesY + Math.abs(eyeHappiness)), 0, -180);
        } else {
            mPathEyeLeft.lineTo(mEyesXLF, mEyesY);
        }

        mPathEyeRight = new Path();
        mPathEyeRight.moveTo(mEyesXR0, mEyesY);
        if (eyeHappiness < -mStrokeWidth / 2) {
            mPathEyeRight.addArc(new RectF(mEyesXR0, mEyesY - Math.abs(eyeHappiness), mEyesXRF, mEyesY + Math.abs(eyeHappiness)), 0, -180);
        } else {
            mPathEyeRight.lineTo(mEyesXRF, mEyesY);
        }
    }

    private void calculateColor() {

        //space for each color
        float spaceColor = (float)(mHappinessLimit * 2) / (colors.length - 1);

        //calculate the first color
        int colorStart = Math.min((int) ((mHappiness + mHappinessLimit) / spaceColor), colors.length-2);
        int colorEnd = colorStart + 1;

        //calculate percentage
        float difSpace = mHappiness + mHappinessLimit - (spaceColor * colorStart);
        int percentage = (int) ((difSpace / spaceColor) * 100);

        //apply the color
        mLinePaint.setColor(Integer.parseInt(new ArgbEvaluator().evaluate(percentage / 100f, colors[colorStart], colors[colorEnd]).toString()));
    }

    // ----- TOUCH -----

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //get y position
        int y = (int)event.getY();

        //calculate the difference
        int difY = mLastY != 0 ? (y - mLastY) : 0;

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //move the mHappiness
            mHappiness += difY * mHappinessSpeed;
            if (mHappiness > mHappinessLimit) {
                mHappiness = mHappinessLimit;
            } else if (mHappiness < -mHappinessLimit) {
                mHappiness = -mHappinessLimit;
            }

            //send event to listener
            if (mListener != null) {

                //calculate the 0-100 value
                int value = ((mHappiness + mHappinessLimit) * 100) / (mHappinessLimit * 2);

                //send value to listener
                mListener.onRatingChange(this, value);
            }
        }

        //save the last position
        mLastY = y;

        //calculate to redraw
        calculate();

        return true;
    }


    // ----- DRAW -----

    @Override
    protected void onDraw(Canvas canvas) {

        //check we have calculations
        if (!mCalculated) {
            return;
        }

        //draw circle
        if (mPathCircle != null) {
            canvas.drawPath(mPathCircle, mLinePaint);
        }

        //draw eyes
        if (mPathEyeLeft != null) {
            canvas.drawPath(mPathEyeLeft, mLinePaint);
        }
        if (mPathEyeRight != null) {
            canvas.drawPath(mPathEyeRight, mLinePaint);
        }

        //draw mouth
        if (mPathMouth != null) {
            canvas.drawPath(mPathMouth, mLinePaint);
        }


    }

    // ----- LISTENER -----

    public interface PeterSmileRatingListener {

        /**
         * Send the smile rating
         * @param peterSmileRating PeterSmileRating reference
         * @param rate int from 0 to 100
         */
        void onRatingChange(PeterSmileRate peterSmileRating, int rate);
    }
}
