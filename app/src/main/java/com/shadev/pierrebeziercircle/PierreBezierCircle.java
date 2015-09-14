package com.shadev.pierrebeziercircle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class PierreBezierCircle extends View {

    private Paint mFillCirclePaint;

    /** View的宽度 **/
    private int width;
    /** View的高度，这里View应该是正方形，所以宽高是一样的 **/
    private int height;
    /** View的中心坐标x **/
    private int centerX;
    /** View的中心坐标y **/
    private int centerY;

    private Path mPath;
    private float diam;
    private float radius;
    private float c;
    private PointF[] mPointF = new PointF[13];
    private float blackMagic = 0.551915024494f;
    private float maxOffset = 30;
    private float maxLength;

    private float mInterpolatedTime;

    public PierreBezierCircle(Context context) {
        this(context, null, 0);
    }

    public PierreBezierCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PierreBezierCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mFillCirclePaint = new Paint();
        mFillCirclePaint.setColor(0xFFfe626d);
        mFillCirclePaint.setStyle(Paint.Style.FILL);
        mFillCirclePaint.setStrokeWidth(1);
        mFillCirclePaint.setAntiAlias(true);
        mPath = new Path();

        for (int i = 0; i < mPointF.length; i++) {
            mPointF[i] = new PointF();
        }
    }

    @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        centerX = width / 2;
        centerY = height / 2;
        diam = width;
        radius = 40;
        c = radius*blackMagic;
        maxLength = width - radius - radius - maxOffset - maxOffset;
        //updatePoint(c,radius,0,maxOffset);
        updatePoint(c, radius, 0, 0);
        //mPointF[0].set(0,radius);
        //mPointF[1].set(c, radius);
        //mPointF[2].set(radius+maxOffset, c);
        //mPointF[3].set(radius+maxOffset, 0);
        //mPointF[4].set(radius+maxOffset, -c);
        //mPointF[5].set(c, -radius);
        //mPointF[6].set(0, -radius);
        //mPointF[7].set(-c, -radius);
        //mPointF[8].set(-radius, -c);
        //mPointF[9].set(-radius, 0);
        //mPointF[10].set(-radius, c);
        //mPointF[11].set(-c, radius);
    }

    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        //canvas.drawCircle(centerX,centerY,centerX-1,mFillCirclePaint);

        //canvas.translate(radius, radius);
        float offset = maxLength*(mInterpolatedTime-0.2f);
        offset = offset>0?offset:0;
        Log.d("demo", "t_offset:" + offset + " right_offset : " + maxOffset * mInterpolatedTime
            + " mInterpolatedTime : " + mInterpolatedTime);
        canvas.translate(radius + maxOffset + offset, centerY);

        //mPath.moveTo(0, radius);
        //mPath.cubicTo(c, radius, radius, c, radius, 0);
        //mPath.cubicTo(radius, -c, c, -radius, 0, -radius);
        //mPath.cubicTo(-c, -radius, -radius, -c, -radius, 0);
        //mPath.cubicTo(-radius, c, -c, radius, 0, radius);
        updatePoint(c, radius, 0, 0);//普通的
        if(!test) {
            mLogic.ctrlRight(mInterpolatedTime);
            mLogic.ctrlTopBottom(mInterpolatedTime);
        }else {
            ctrlRight(30);
            ctrlTopBottom(10);
        }
        //updatePoints(c, radius, 0, maxOffset, mInterpolatedTime);//
        drawPath();

        canvas.drawPath(mPath, mFillCirclePaint);

        test=false;
        //canvas.translate(radius,radius);
        //canvas.drawCircle(0,0,centerX-1,mFillCirclePaint);
    }

    private void drawPath(){
        mPath.moveTo(mPointF[0].x, mPointF[0].y);
        mPath.cubicTo(mPointF[1].x, mPointF[1].y, mPointF[2].x, mPointF[2].y, mPointF[3].x,
            mPointF[3].y);
        mPath.cubicTo(mPointF[4].x, mPointF[4].y, mPointF[5].x, mPointF[5].y, mPointF[6].x,
            mPointF[6].y);
        mPath.cubicTo(mPointF[7].x, mPointF[7].y, mPointF[8].x, mPointF[8].y, mPointF[9].x,
            mPointF[9].y);
        mPath.cubicTo(mPointF[10].x, mPointF[10].y, mPointF[11].x, mPointF[11].y, mPointF[0].x,
            mPointF[0].y);
    }

    /**
     *
     * @param c 经过魔法数字处理的值
     * @param radius 半径
     * @param leftOffset 左边偏移值,为0则代表不使用
     * @param rightOffset 右边偏移值,为0则代表不使用
     */
    private void updatePoint(float c,float radius,float leftOffset,float rightOffset){
        mPointF[0].set(0,radius);
        mPointF[1].set(c, radius);
        mPointF[2].set(radius+rightOffset, c);
        mPointF[3].set(radius+rightOffset, 0);
        mPointF[4].set(radius+rightOffset, -c);
        mPointF[5].set(c, -radius);
        mPointF[6].set(0, -radius);
        mPointF[7].set(-c, -radius);
        mPointF[8].set(-radius - leftOffset, -c);
        mPointF[9].set(-radius - leftOffset, 0);
        mPointF[10].set(-radius - leftOffset, c);
        mPointF[11].set(-c, radius);
    }

    /**
     *
     * @param c 经过魔法数字处理的值
     * @param radius 半径
     * @param leftMaxOffset 左边最大偏移值
     * @param rightMaxOffset 右边最大偏移值
     * @param time 0f~1f 代表时间轴,一秒
     */
    private void updatePoints(float c,float radius,float leftMaxOffset,float rightMaxOffset,float time){
        mPointF[2].set(radius+(Math.min(rightMaxOffset, rightMaxOffset * time * 5)), c);
        mPointF[3].set(radius+(Math.min(rightMaxOffset, rightMaxOffset * time * 5)), 0);
        mPointF[4].set(radius+(Math.min(rightMaxOffset,rightMaxOffset * time * 5)), -c);

        mPointF[8].set(-radius-leftMaxOffset, -c);
        mPointF[9].set(-radius-leftMaxOffset, 0);
        mPointF[10].set(-radius-leftMaxOffset, c);

        mPointF[5].set(c, -radius+vOffset*time);
        mPointF[6].set(0, -radius + vOffset * time);
        mPointF[7].set(-c, -radius + vOffset * time);

        mPointF[0].set(0, radius - vOffset * time);
        mPointF[1].set(c, radius - vOffset * time);
        mPointF[11].set(-c, radius - vOffset * time);
    }

    private void ctrlRight(float rightOffset){
        mPointF[2].set(radius+rightOffset, c);
        mPointF[3].set(radius+rightOffset, 0);
        mPointF[4].set(radius+rightOffset, -c);
    }

    private void ctrlRightV(float offset){
        mPointF[2].y +=offset;
        mPointF[4].y -=offset;
    }

    private void ctrlTopBottom(float offset){
        mPointF[5].set(c, -radius+offset);
        mPointF[6].set(0, -radius+offset);
        mPointF[7].set(-c, -radius+offset);

        mPointF[0].set(0,radius-offset);
        mPointF[1].set(c, radius-offset);
        mPointF[11].set(-c, radius-offset);
    }

    private float vOffset = 40;

    private class MoveAnimation extends Animation {

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mInterpolatedTime = interpolatedTime;
            invalidate();
        }
    }

    public void startAnimation() {
        mPath.reset();
        mInterpolatedTime = 0;
        MoveAnimation move = new MoveAnimation();
        move.setDuration(2500);
        move.setInterpolator(new AccelerateDecelerateInterpolator());
        //move.setRepeatCount(Animation.INFINITE);
        //move.setRepeatMode(Animation.REVERSE);
        startAnimation(move);
    }

    private BusinessLogic mLogic = new BusinessLogic(this);
    class BusinessLogic {
        PierreBezierCircle view;

        public BusinessLogic(PierreBezierCircle view) {
            this.view = view;
        }

        public void ctrlRight(float time){
            //float offset = time*5*view.maxOffset;// ∵ 0<time<1, 0<time*5<1 ∴ 0<time<0.2 这里将移动时间控制在0.2s的时候完成
            float offset = transTime(time,0,0.2f)*view.maxOffset;// ∵ 0<time<1, 0<time*5<1 ∴ 0<time<0.2 这里将移动时间控制在0.2s的时候完成
            offset = Math.min(offset,maxOffset);
            view.ctrlRight(offset);

            Log.d("demo","logic  ctrlRight : " + offset);
        }

        public void ctrlTopBottom(float time) {
            //if(time>=0.2&&time<=0.6){
            //    time = (time-0.2f)*2.5f;
            //}else if(time<0.2){
            //    time = (0.2f-0.2f)*2.5f;
            //}else if(time>0.6){
            //    time = (0.6f-0.2f)*2.5f;
            //}
            time = Math.min(Math.max(0.2f,time),0.6f);
            time = transTime(time,0.2f,0.6f);
            float offset = time * 10;
            float offset2 = time * 10;
            view.ctrlTopBottom(offset);
            view.ctrlRightV(offset2);
            Log.d("demo", "logic  ctrlTopBottom : " + offset);

        }

        public float transTime(float time,float start,float end){
            return (time-start)*(1f/(end-start));
        }
    }

    private boolean test = false;
    public void test(){
        mPath.reset();
        test = true;
        invalidate();
    }
}
