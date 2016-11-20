package com.shadev.pierrebeziercircle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 原作者相关教程
 * http://www.jianshu.com/p/791d3a791ec2
 * <p/>
 * 我是来填坑的
 */
public class MagicCircle extends View {
	//三阶贝塞尔绘制圆形需要用到的黑魔法
	private static final float BLACK_MAGIC = 0.551915024494f;

	private Path mPath;
	private Paint mFillCirclePaint;

	//View的尺寸,View应该是正方形，所以宽高是一样的
	private int mWidth;
	private int mHeight;

	//View的中心坐标
	private int mCenterX;
	private int mCenterY;

	private float mMaxLength;
	private float mInterpolatedTime;
	private float mStretchDistance;
	private float mDistance;
	private float mRadius;
	//控制点，需与BLACK_MAGIC配合
	private float mMagicControl;

	//上下两个点
	private HPoint mPoint1, mPoint3;
	//左右两个点
	private VPoint mPoint2, mPoint4;

	public MagicCircle(Context context) {
		this(context, null, 0);
	}

	public MagicCircle(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MagicCircle(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		mFillCirclePaint = new Paint();
		mFillCirclePaint.setColor(0xFFfe626d);
		mFillCirclePaint.setStyle(Paint.Style.FILL);
		mFillCirclePaint.setAntiAlias(true);
		mFillCirclePaint.setDither(true);

		mPath = new Path();

		mPoint2 = new VPoint();
		mPoint4 = new VPoint();

		mPoint1 = new HPoint();
		mPoint3 = new HPoint();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mWidth = w;
		mHeight = h;
		mCenterX = mWidth / 2;
		mCenterY = mHeight / 2;
		mRadius = 50;
		mMagicControl = mRadius * BLACK_MAGIC;
		mStretchDistance = mRadius;
		mDistance = mMagicControl * 0.45f;
		mMaxLength = mWidth - mRadius * 2;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPath.reset();
		//将画布坐标轴中心移至初始圆心
		canvas.translate(mRadius, mRadius);

		//分阶段设置控制点和数据点坐标
		if (mInterpolatedTime >= 0 && mInterpolatedTime <= 0.2) {
			model1(mInterpolatedTime);
		} else if (mInterpolatedTime > 0.2 && mInterpolatedTime <= 0.5) {
			model2(mInterpolatedTime);
		} else if (mInterpolatedTime > 0.5 && mInterpolatedTime <= 0.8) {
			model3(mInterpolatedTime);
		} else if (mInterpolatedTime > 0.8 && mInterpolatedTime <= 0.9) {
			model4(mInterpolatedTime);
		} else if (mInterpolatedTime > 0.9 && mInterpolatedTime <= 1) {
			model5(mInterpolatedTime);
		}

		//实时改变控制点的x坐标
		float offset = mMaxLength * (mInterpolatedTime - 0.2f);
		offset = offset > 0 ? offset : 0;
		mPoint1.adjustAllX(offset);
		mPoint2.adjustAllX(offset);
		mPoint3.adjustAllX(offset);
		mPoint4.adjustAllX(offset);

		//设置路径，可参考教程
		mPath.moveTo(mPoint1.x, mPoint1.y);
		mPath.cubicTo(mPoint1.right.x, mPoint1.right.y, mPoint2.bottom.x, mPoint2.bottom.y, mPoint2.x, mPoint2.y);
		mPath.cubicTo(mPoint2.top.x, mPoint2.top.y, mPoint3.right.x, mPoint3.right.y, mPoint3.x, mPoint3.y);
		mPath.cubicTo(mPoint3.left.x, mPoint3.left.y, mPoint4.top.x, mPoint4.top.y, mPoint4.x, mPoint4.y);
		mPath.cubicTo(mPoint4.bottom.x, mPoint4.bottom.y, mPoint1.left.x, mPoint1.left.y, mPoint1.x, mPoint1.y);

		//绘制路径，好吧，这句话是多余的
		canvas.drawPath(mPath, mFillCirclePaint);
	}

	/**
	 * 设置初始状态路径
	 * 对应教程状态1
	 */
	private void model0() {
		mPoint1.setY(mRadius);
		mPoint3.setY(-mRadius);
		mPoint3.x = mPoint1.x = 0;
		mPoint3.left.x = mPoint1.left.x = -mMagicControl;
		mPoint3.right.x = mPoint1.right.x = mMagicControl;

		mPoint2.setX(mRadius);
		mPoint4.setX(-mRadius);
		mPoint2.y = mPoint4.y = 0;
		mPoint2.top.y = mPoint4.top.y = -mMagicControl;
		mPoint2.bottom.y = mPoint4.bottom.y = mMagicControl;
	}

	/**
	 * 设置状态1路径，根据时间点不同，数据点的x坐标发生对应变化
	 * 对应教程状态2
	 *
	 * @param time 时间点
	 */
	private void model1(float time) {//0~0.2
		model0();

		mPoint2.setX(mRadius + mStretchDistance * time * 5);
	}

	/**
	 * 设置状态2路径，根据时间点不同，数据点的x坐标发生对应变化
	 * 对应教程状态3
	 *
	 * @param time 时间点
	 */
	private void model2(float time) {//0.2~0.5
		model1(0.2f);
		//将时间变化值调整为0-1，便于adjustAllX和adjustY根据比例计算偏移值
		//后同
		time = (time - 0.2f) * (10f / 3);

		mPoint1.adjustAllX(mStretchDistance / 2 * time);
		mPoint3.adjustAllX(mStretchDistance / 2 * time);

		mPoint2.adjustY(mDistance * time);
		mPoint4.adjustY(mDistance * time);
	}

	/**
	 * 设置状态3路径，根据时间点不同，数据点的x坐标发生对应变化
	 * 对应教程状态3
	 *
	 * @param time 时间点
	 */
	private void model3(float time) {//0.5~0.8
		model2(0.5f);
		time = (time - 0.5f) * (10f / 3);

		mPoint1.adjustAllX(mStretchDistance / 2 * time);
		mPoint3.adjustAllX(mStretchDistance / 2 * time);

		mPoint2.adjustY(-mDistance * time);
		mPoint4.adjustY(-mDistance * time);

		mPoint4.adjustAllX(mStretchDistance / 2 * time);
	}

	/**
	 * 设置状态4路径，根据时间点不同，数据点的x坐标发生对应变化
	 * 对应教程状态4
	 *
	 * @param time 时间点
	 */
	private void model4(float time) {//0.8~0.9
		model3(0.8f);
		time = (time - 0.8f) * 10;

		mPoint4.adjustAllX(mStretchDistance / 2 * time);
	}

	/**
	 * 设置状态5路径，根据时间点不同，数据点的x坐标发生对应变化
	 * 对应教程状态5，有回弹效果
	 *
	 * @param time 时间点
	 */
	private void model5(float time) {
		model4(0.9f);
		time = time - 0.9f;

		mPoint4.adjustAllX((float) (Math.sin(Math.PI * time * 10f) * (2 / 10f * mRadius)));
	}

	public void startAnimation() {
		mPath.reset();
		mInterpolatedTime = 0;
		MoveAnimation move = new MoveAnimation();
		move.setDuration(1000);
		move.setInterpolator(new AccelerateDecelerateInterpolator());
		move.setRepeatCount(Animation.INFINITE);
		move.setRepeatMode(Animation.REVERSE);
		startAnimation(move);
	}

	class VPoint {
		public float x;
		public float y;
		public PointF top = new PointF();
		public PointF bottom = new PointF();

		public void setX(float x) {
			this.x = x;
			top.x = x;
			bottom.x = x;
		}

		public void adjustY(float offset) {
			top.y -= offset;
			bottom.y += offset;
		}

		public void adjustAllX(float offset) {
			this.x += offset;
			top.x += offset;
			bottom.x += offset;
		}
	}

	class HPoint {
		public float x;
		public float y;
		public PointF left = new PointF();
		public PointF right = new PointF();

		public void setY(float y) {
			this.y = y;
			left.y = y;
			right.y = y;
		}

		public void adjustAllX(float offset) {
			this.x += offset;
			left.x += offset;
			right.x += offset;
		}
	}

	private class MoveAnimation extends Animation {

		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			mInterpolatedTime = interpolatedTime;
			invalidate();
		}
	}
}
