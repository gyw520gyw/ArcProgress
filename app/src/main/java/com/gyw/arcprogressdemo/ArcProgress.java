package com.gyw.arcprogressdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;


/**
 * @author gyw
 * @version 1.0
 * @time: 2015-9-14 下午4:52:15
 * @fun:
 */

public class ArcProgress extends View {
	private Paint paint;
	protected Paint textPaint;
	private Paint innerPaint;

	private RectF rectF = new RectF();
	
	private float strokeWidth;
	private float suffixTextSize;
	private float bottomTextSize;
	private String bottomText;
	private float textSize;
	private int textColor;
	private int progress = 0;
	private int max;
	private int finishedStrokeColor;
	private int unfinishedStrokeColor;
	private float arcAngle;
	private String suffixText = "步";
	private float suffixTextPadding;

	private float innerRadius;
	private float cx;
	private float cy;

	private final int default_finished_color = Color.WHITE;
	private final int default_unfinished_color = Color.rgb(72, 106, 176);
	private final int default_text_color = Color.rgb(66, 145, 241);
	private final float default_suffix_text_size;
	private final float default_suffix_padding;
	private final float default_bottom_text_size;
	private final float default_stroke_width;
	private final String default_suffix_text;
	private final int default_max = 100;
	private final float default_arc_angle = 360 * 1.0f;	// 这里可以控制圆环的闭合度
	private float default_text_size;
	private final int min_size;


	public ArcProgress(Context context) {
		this(context, null);
	}

	public ArcProgress(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ArcProgress(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		default_text_size = DisplayUtil.sp2px(context, 18);
		min_size = (int) DisplayUtil.dip2px(context, 100);
		default_text_size = DisplayUtil.sp2px(context, 40);
		default_suffix_text_size = DisplayUtil.sp2px(context, 15);
		default_suffix_padding = DisplayUtil.dip2px(context, 4);
		default_suffix_text = "步";
		default_bottom_text_size = DisplayUtil.sp2px(context, 10);
		default_stroke_width = DisplayUtil.dip2px(context, 4);

		TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcProgress, defStyleAttr, 0);
		initByAttributes(attributes);
		attributes.recycle();

		initPainters();
	}

	protected void initByAttributes(TypedArray attributes) {
		finishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_finished_color, default_finished_color);
		unfinishedStrokeColor = attributes.getColor(R.styleable.ArcProgress_arc_unfinished_color, default_unfinished_color);
		textColor = attributes.getColor(R.styleable.ArcProgress_arc_text_color, default_text_color);
		textSize = attributes.getDimension(R.styleable.ArcProgress_arc_text_size, default_text_size);
		arcAngle = attributes.getDimension(R.styleable.ArcProgress_arc_angle, default_arc_angle);
		setMax(attributes.getInt(R.styleable.ArcProgress_arc_max, default_max));
		setProgress(attributes.getInt(R.styleable.ArcProgress_arc_progress, 0));
		strokeWidth = attributes.getDimension(R.styleable.ArcProgress_arc_stroke_width, default_stroke_width);
		suffixTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_suffix_text_size, default_suffix_text_size);
		suffixText = TextUtils.isEmpty(attributes.getString(R.styleable.ArcProgress_arc_suffix_text)) ? default_suffix_text : attributes
				.getString(R.styleable.ArcProgress_arc_suffix_text);
		suffixTextPadding = attributes.getDimension(R.styleable.ArcProgress_arc_suffix_text_padding, default_suffix_padding);
		bottomTextSize = attributes.getDimension(R.styleable.ArcProgress_arc_bottom_text_size, default_bottom_text_size);
		bottomText = attributes.getString(R.styleable.ArcProgress_arc_bottom_text);
	}

	protected void initPainters() {
		textPaint = new TextPaint();
		textPaint.setColor(textColor);
		textPaint.setTextSize(textSize);
		textPaint.setAntiAlias(true);

		paint = new Paint();
		paint.setColor(default_unfinished_color);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(strokeWidth);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeCap(Paint.Cap.ROUND);

		innerPaint = new Paint();
		innerPaint.setColor(finishedStrokeColor);
		innerPaint.setAntiAlias(true);
		innerPaint.setStrokeWidth((float) (strokeWidth * 0.2));
		innerPaint.setStyle(Paint.Style.STROKE);
	}

	@Override
	public void invalidate() {
		initPainters();
		super.invalidate();
	}

	public float getStrokeWidth() {
		return strokeWidth;
	}

	public void setStrokeWidth(float strokeWidth) {
		this.strokeWidth = strokeWidth;
		this.invalidate();
	}

	public float getSuffixTextSize() {
		return suffixTextSize;
	}

	public void setSuffixTextSize(float suffixTextSize) {
		this.suffixTextSize = suffixTextSize;
		this.invalidate();
	}

	public String getBottomText() {
		return bottomText;
	}

	public void setBottomText(String bottomText) {
		this.bottomText = bottomText;
		this.invalidate();
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
		// if (this.progress > getMax()) {
		// this.progress %= getMax();
		// }
		invalidate();
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		if (max > 0) {
			this.max = max;
			invalidate();
		}
	}

	public float getBottomTextSize() {
		return bottomTextSize;
	}

	public void setBottomTextSize(float bottomTextSize) {
		this.bottomTextSize = bottomTextSize;
		this.invalidate();
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
		this.invalidate();
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
		this.invalidate();
	}

	public int getFinishedStrokeColor() {
		return finishedStrokeColor;
	}

	public void setFinishedStrokeColor(int finishedStrokeColor) {
		this.finishedStrokeColor = finishedStrokeColor;
		this.invalidate();
	}

	public int getUnfinishedStrokeColor() {
		return unfinishedStrokeColor;
	}

	public void setUnfinishedStrokeColor(int unfinishedStrokeColor) {
		this.unfinishedStrokeColor = unfinishedStrokeColor;
		this.invalidate();
	}

	public float getArcAngle() {
		return arcAngle;
	}

	public void setArcAngle(float arcAngle) {
		this.arcAngle = arcAngle;
		this.invalidate();
	}

	public String getSuffixText() {
		return suffixText;
	}

	public void setSuffixText(String suffixText) {
		this.suffixText = suffixText;
		this.invalidate();
	}

	public float getSuffixTextPadding() {
		return suffixTextPadding;
	}

	public void setSuffixTextPadding(float suffixTextPadding) {
		this.suffixTextPadding = suffixTextPadding;
		this.invalidate();
	}

	@Override
	protected int getSuggestedMinimumHeight() {
		return min_size;
	}

	@Override
	protected int getSuggestedMinimumWidth() {
		return min_size;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		rectF.set(strokeWidth / 2f, strokeWidth / 2f, MeasureSpec.getSize(widthMeasureSpec) - strokeWidth / 2f, MeasureSpec.getSize(heightMeasureSpec)
				- strokeWidth / 2f);
		float radius = getWidth() / 2f;
		innerRadius = (float) (radius * 0.8);
		cx = getWidth() / 2;
		cy = getHeight() / 2;
		// float angle = (360 - arcAngle) / 2f;
		// arcBottomHeight = radius * (float) (1 - Math.cos(angle / 180 *
		// Math.PI));
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float startAngle = 270 - arcAngle / 2f;
		float finishedSweepAngle = progress / (float) getMax() * arcAngle;
		if (this.progress > getMax()) {
			finishedSweepAngle = arcAngle;
		}
		float finishedStartAngle = startAngle;
		paint.setColor(unfinishedStrokeColor);
		canvas.drawArc(rectF, startAngle, arcAngle, false, paint);
		paint.setColor(finishedStrokeColor);
		canvas.drawArc(rectF, finishedStartAngle, finishedSweepAngle, false, paint);

		canvas.drawCircle(cx, cy, innerRadius, innerPaint);

		String text = String.valueOf(getProgress());
		if (!TextUtils.isEmpty(text)) {
			textPaint.setColor(textColor);
			textPaint.setTextSize(textSize);
			if (isStopStep) {
				text = "暂停";
				textPaint.setTextSize(textSize / 2);
			}
			float textHeight = textPaint.descent() + textPaint.ascent();
			float textBaseline = (getHeight() - textHeight) / 2.0f;
			canvas.drawText(text, (getWidth() - textPaint.measureText(text)) / 2.0f, textBaseline, textPaint);

			textPaint.setTextSize(suffixTextSize);

			float suffixHeight = (float) ((getHeight() * 0.3) - (textPaint.descent() + textPaint.ascent()) / 2);
			canvas.drawText(suffixText, (getWidth() - textPaint.measureText(suffixText)) / 2.0f, suffixHeight, textPaint);
		}

		if (!TextUtils.isEmpty(getBottomText())) {
			textPaint.setTextSize(bottomTextSize);
			float bottomTextBaseline = (float) ((getHeight() * 0.7) - (textPaint.descent() + textPaint.ascent()) / 2);
			canvas.drawText(getBottomText(), (getWidth() - textPaint.measureText(getBottomText())) / 2.0f, bottomTextBaseline, textPaint);
		}
	}



	private boolean isStopStep = false;

	public void setStopStep(boolean isStopStep) {
		this.isStopStep = isStopStep;
		this.invalidate();
	}
}
