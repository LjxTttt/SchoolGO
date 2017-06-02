package newer.com.schoolgo.ui.diyview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import newer.com.schoolgo.R;

/**
 * Created by Administrator on 2017/4/11.
 */

public class PullRefreshView extends RelativeLayout {
    protected View mHeadView;
    protected TextView txt;
    protected Scroller mLayoutScroller;
    protected Context context;
    private int LastMoveY;
    private int mMaxScrollHeight = 200;
    private int mContentView = 0;

    public PullRefreshView(Context context) {
        super(context);
        initView(context);
    }

    public PullRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PullRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void initView(Context context) {
        mLayoutScroller = new Scroller(context);
        mHeadView = LayoutInflater.from(context).inflate(R.layout.refresh_top, null);
    }

    @Override
    protected void onFinishInflate() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mHeadView, layoutParams);
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof ProgressBar) {
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                getChildAt(i).setLayoutParams(layoutParams);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录按下时的y值
                LastMoveY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //向上滑动时最后记录的y大于现在的y
                //向下滑动时最后记录的y小于现在的y
                int dy = LastMoveY - y;
                if (dy < 0) {
                    if (Math.abs(getScrollY()) <= mHeadView.getMeasuredHeight() / 2) {
                        scrollBy(0, dy);
                        Log.d("TAG", "Math.abs(getScrollY())====" + Math.abs(getScrollY()));
                        if (Math.abs(getScrollY()) >= mMaxScrollHeight) {
                            txt = (TextView) mHeadView.findViewById(R.id.tv);
                            txt.setText("松开刷新");
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(getScrollY()) >= mMaxScrollHeight) {
                    mLayoutScroller.startScroll(0, getScrollY(), 0, -(getScrollY() + mMaxScrollHeight));
                } else {
                    mLayoutScroller.startScroll(0, getScrollY(), 0, -getScrollY());
                }
                invalidate();
                break;
        }
        LastMoveY = y;
        return true;
    }

    @Override
    public void computeScroll() {
        if (mLayoutScroller.computeScrollOffset()) {
            scrollTo(mLayoutScroller.getCurrX(), mLayoutScroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child == mHeadView) {
                child.layout(0, 0 - child.getMeasuredHeight(), child.getMeasuredWidth(),
                        0);
            } else if (child instanceof ProgressBar) {
                //对中间的ProgessBar进行居中处理
                int centerLayout = child.getRootView().getMeasuredHeight() / 2 - child.getMeasuredHeight() / 2;
                child.layout(0, centerLayout, child.getMeasuredWidth(),
                        centerLayout + child.getMeasuredHeight());
            } else {
                child.layout(0, mContentView, child.getMeasuredWidth(),
                        mContentView + child.getMeasuredHeight());
            }
        }
    }
}
