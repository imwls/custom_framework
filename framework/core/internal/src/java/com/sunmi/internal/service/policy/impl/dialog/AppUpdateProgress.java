package com.sunmi.internal.service.policy.impl.dialog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class AppUpdateProgress extends View {
	// 画笔对象的引用
	private Paint mRingPaint = new Paint();
	private Paint mTxtPaint = new Paint();
	private Paint mRoundPaint = new Paint();

	// 坐标
	private int mCentre;
	private int mRadius;
	private RectF mOval;

	// 显示内容
	private int mMax;
	private int mProgress;
	private String mMaxStr;
	private String mProgressStr;

	/**
	 * 圆环的颜色
	 */
	private int roundColor;

	/**
	 * 圆环进度的颜色
	 */
	private int roundProgressColor;

	/**
	 * 中间进度百分比的字符串的颜色
	 */
	private int textColor;

	/**
	 * 中间进度百分比的字符串的字体
	 */
	private float textSize;

	/**
	 * 圆环的宽度
	 */
	private float roundWidth;

	public AppUpdateProgress(Context context) {
		this(context, null);
	}

	public AppUpdateProgress(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public AppUpdateProgress(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// 获取自定义属性和默认值
		roundColor = Color.parseColor("#F4F4F4");
		roundProgressColor = Color.parseColor("#FF6900");
		textColor = Color.parseColor("#FF6900");
		textSize = 18;
		roundWidth = 4;
		mMax = 100;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setupBounds();
		setupPaints();
		invalidate();
	}

	private void setupPaints() {
		mRingPaint.setColor(roundColor); // 设置圆环的颜色
		mRingPaint.setStyle(Paint.Style.STROKE); // 设置空心
		mRingPaint.setStrokeWidth(roundWidth); // 设置圆环的宽度
		mRingPaint.setAntiAlias(true); // 消除锯齿

		mTxtPaint.setColor(textColor);
		mTxtPaint.setStyle(Style.FILL);
		mTxtPaint.setAntiAlias(true);
		mTxtPaint.setTextSize(textSize);

		mRoundPaint.setColor(roundProgressColor);
		mRoundPaint.setStyle(Paint.Style.STROKE);
		mRoundPaint.setStrokeWidth(roundWidth);
		mRoundPaint.setAntiAlias(true);
	}

	private void setupBounds() {
		mCentre = getWidth() / 2; // 获取圆心的x坐标
		mRadius = (int) (mCentre - roundWidth / 2); // 圆环的半径
		mOval = new RectF(mCentre - mRadius, mCentre - mRadius, mCentre + mRadius, mCentre + mRadius);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 圆环
		canvas.drawCircle(mCentre, mCentre, mRadius, mRingPaint); // 画出圆环

		// 圆环刻度
		canvas.drawArc(mOval, 270, 360 * mProgress / mMax, false, mRoundPaint); // 根据进度画圆弧

		// 文字
		canvas.drawText(mProgressStr, mCentre - mTxtPaint.measureText(mProgressStr) / 2, mCentre, mTxtPaint);
		canvas.drawText(mMaxStr, mCentre - mTxtPaint.measureText(mMaxStr) / 2, mCentre + textSize + 3, mTxtPaint);
	}

	public synchronized void setInfo(int progress, int max, String progressStr, String maxStr) {
		if (max < 0 || progress < 0) {
			throw new IllegalArgumentException("max not less than 0");
		}
		this.mMax = max;
		if (progress > mMax) {
			progress = mMax;
		}
		if (progress <= mMax) {
			this.mProgress = progress;
		}
		mProgressStr = progressStr;
		mMaxStr = maxStr;
		postInvalidate();
	}
}
