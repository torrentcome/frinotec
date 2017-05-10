package torrentcome.frinotec.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import torrentcome.frinotec.R;

public class BaseBarView extends View {

    private Paint mRectPaint;
    private Paint mLinePaint;
    private RectF mRectBounds = new RectF();
    private int mColorLeft;
    private int mColorRight;
    private int mColorDefault;
    private int left, right;
    private int top, bottom;

    private ObjectAnimator mAnimatorY;
    private float mPhase = 1f;
    private Path mPath;

    public BaseBarView(Context context) {
        super(context);
        init(context, null);
    }

    public BaseBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BaseBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRectPaint.setColor(mColorDefault);
        mRectBounds.set(0, 0, getWidth(), getHeight());
        canvas.drawRect(mRectBounds, mRectPaint);
        drawRectangleTop(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = Math.max(minw, MeasureSpec.getSize(widthMeasureSpec));

        int minh = getPaddingBottom() + getPaddingTop();
        int h = Math.max(MeasureSpec.getSize(heightMeasureSpec), minh);

        mRectBounds.set(0,
                0,
                w,
                h);

        setMeasuredDimension(w, h);
    }

    /**
     * Set the animation phase.
     *
     * @param phase Phase.
     */
    public void setPhase(float phase) {
        mPhase = phase;
    }

    /**
     * call animateStats with a default parameter
     */
    public void animateStats() {
        animateStats(1000);
    }

    /**
     * Animate the phase from 0f to 1f according to the duration.
     *
     * @param millisec Duration in milliseconds.
     */
    public void animateStats(int millisec) {
        mAnimatorY = ObjectAnimator.ofFloat(this, "phase", 0f, 1f);
        mAnimatorY.setDuration(millisec);
        mAnimatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        mAnimatorY.start();
    }

    private void init(Context context, AttributeSet attrs) {
        final Resources resources = context.getResources();
        mColorLeft = resources.getColor(R.color.colorAccent);
        mColorRight = resources.getColor(R.color.colorAccent);
        mColorDefault = resources.getColor(R.color.colorPrimaryLight);
        mPath = new Path();
        mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectPaint.setStyle(Paint.Style.FILL);
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRectBounds = new RectF();
        setWillNotDraw(false);
        top = 100;
    }

    private void drawRectangleLeft(Canvas canvas) {
        int total = this.right + this.left;
        int height = getHeight();
        mLinePaint.setColor(mColorLeft);
        if (total > 0) {
            mRectBounds.set(0,
                    0,
                    mPhase * (getWidth() * this.left / total),
                    getHeight());
            canvas.drawRect(mRectBounds, mLinePaint);
        } else if (total == 0) {
            mRectBounds.set(0,
                    0,
                    mPhase * (getWidth() * 1 / 2),
                    getHeight());
            canvas.drawRect(mRectBounds, mLinePaint);
            drawTriangleLeft(canvas, (int) (mPhase * (getWidth() * 1 / 2)), height);
        }
    }

    private void drawRectangleTop(Canvas canvas) {
        int total = this.top + this.bottom;
        mLinePaint.setColor(mColorLeft);
        if (total > 0) {
            mRectBounds.set(0,
                    0,
                    getWidth(),
                    mPhase * (getHeight() * this.top / total));
            canvas.drawRect(mRectBounds, mLinePaint);
        }
    }

    private void drawRectangleRight(Canvas canvas) {
        int total = this.right + this.left;
        int height = getHeight();
        mLinePaint.setColor(mColorRight);
        if (total > 0) {
            mRectBounds.set(getWidth() - mPhase * (getWidth() * right / total) + 20,
                    0,
                    getWidth(),
                    getHeight());

            canvas.drawRect(mRectBounds, mLinePaint);
        } else if (total == 0) {
            mRectBounds.set(getWidth() - mPhase * (getWidth() * 1 / 2) + 20,
                    0,
                    getWidth(),
                    getHeight());
            canvas.drawRect(mRectBounds, mLinePaint);
            drawTriangleRight(canvas, (int) (getWidth() - mPhase * (getWidth() * 1 / 2)) + 20, height);
        }

    }

    private void drawTriangleLeft(Canvas canvas, int x, int y) {
        mLinePaint.setStrokeWidth(0);
        mLinePaint.setColor(mColorLeft);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPath.reset();
        mPath.setFillType(Path.FillType.EVEN_ODD);
        mPath.moveTo(x, 0);
        mPath.lineTo(x, y);
        mPath.lineTo(x + 20, 0);
        mPath.lineTo(x, 0);
        mPath.close();

        canvas.drawPath(mPath, mLinePaint);
    }

    private void drawTriangleRight(Canvas canvas, int x, int y) {
        mLinePaint.setStrokeWidth(0);
        mLinePaint.setColor(mColorRight);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPath.reset();
        mPath.setFillType(Path.FillType.EVEN_ODD);
        mPath.moveTo(x + 1, y);
        mPath.lineTo(x - 20, y);
        mPath.lineTo(x + 1, 0);
        mPath.lineTo(x + 1, y);
        mPath.close();
        canvas.drawPath(mPath, mLinePaint);
    }
}