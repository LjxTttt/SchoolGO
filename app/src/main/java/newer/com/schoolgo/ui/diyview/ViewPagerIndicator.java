package newer.com.schoolgo.ui.diyview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import newer.com.schoolgo.R;

/**
 * Created by 樱花满地，集于我心 on 2016/10/17 0017.
 */
public class ViewPagerIndicator extends LinearLayout {
    private static final int DEFAULT_TABCOUNT = 3;
    private int scroolState = 0;
    private int titleViewWidth, titleViewHeight;   //这是一个title的宽度和高度
    private int translationX; //title改变位置的值
    private int offsetX;  //设置偏移量
    private Paint paint;
    private int tabVisibleCount;  //显示屏幕内可显示的title数量
    private int lineColor = 0xFFFFFFFF;   //下划线的颜色
    private int txtNormlColor = 0xFFBFBFBF;  //字体颜色
    private int txtSelectColor = 0xFFFFFFFF;  //选中时字体颜色
    private int txtSize = 16;  //字体大小
    private ViewPager mViewPager;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取自定义属性里面的Tab可见数量
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        tabVisibleCount = typedArray.getInt(R.styleable.ViewPagerIndicator_visible_tab_count, DEFAULT_TABCOUNT);
        if (tabVisibleCount <= 0) {
            tabVisibleCount = DEFAULT_TABCOUNT;
        }
        lineColor = typedArray.getColor(R.styleable.ViewPagerIndicator_lineColor, lineColor);
        txtNormlColor = typedArray.getColor(R.styleable.ViewPagerIndicator_txtColor, txtNormlColor);
        txtSelectColor = typedArray.getColor(R.styleable.ViewPagerIndicator_txtSelectColor, txtSelectColor);
        typedArray.recycle();
        //初始化画笔
        paint = new Paint();
        paint.setColor(lineColor);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        paint.setStrokeWidth(titleViewHeight * 1 / 5);  //线的粗细为盒子高度的1/5
        int lineX = titleViewWidth / 2;
        canvas.translate(translationX, 0);
        canvas.drawLine(lineX - offsetX, titleViewHeight, lineX + offsetX, titleViewHeight, paint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        titleViewWidth = w / tabVisibleCount;     //计算每个标题盒子所需要的宽度
        titleViewHeight = h;      //标题的高度
        offsetX = titleViewWidth * 1 / 5;
    }

    public void scroll(int position, float positionOffset) {
        int tabWidth = getMeasuredWidth() / tabVisibleCount;
        translationX = (int) (tabWidth * (position + positionOffset));
        if (position >= (tabVisibleCount - 2) && positionOffset > 0 && getChildCount() > tabVisibleCount) {
            if (tabVisibleCount != 1) {
                scrollTo((position - (tabVisibleCount - 2)) * tabWidth + (int) (tabWidth * positionOffset), 0);
            } else {
                scrollTo(position * tabWidth + (int) (tabWidth * positionOffset), 0);
            }
        }
        if (scroolState == 2) {
            new LineSmoothMoveThread().start();
        } else if (scroolState == 1) {
            offsetX = (titleViewWidth / tabVisibleCount) * 1 / 3;
        }
        invalidate();
    }

    public void scrollChangeState(int state) {
        scroolState = state;
        if (scroolState == 0) {
            offsetX = titleViewWidth * 1 / 5;
        }
        invalidate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if (childCount == 0) return;
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            LayoutParams lp = (LayoutParams) view.getLayoutParams();
            lp.weight = 0;
            lp.width = getScreenWidth() / tabVisibleCount;
            view.setLayoutParams(lp);
        }
    }

    //获取屏幕宽度
    private int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public void setTabTitleItems(List<String> items) {
        if (items != null && items.size() != 0) {
            removeAllViews();
            for (String title : items) {
                addView(generateTextView(title));
            }
            selectTxtColor(0);
            setItemTextClick();
        }
    }

    private View generateTextView(String title) {
        TextView tv = new TextView(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.width = getScreenWidth() / tabVisibleCount;
        tv.setText(title);
        tv.setTextColor(txtNormlColor);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, txtSize);
        tv.setLayoutParams(lp);
        return tv;
    }

    public void selectTxtColor(int pos) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView && i == pos) {
                ((TextView) view).setTextColor(txtSelectColor);
            } else if (view instanceof TextView) {
                ((TextView) view).setTextColor(txtNormlColor);
            }
        }
    }

    private void setItemTextClick() {
        for (int i = 0; i < getChildCount(); i++) {
            final int j = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }

    public void setViewPager(ViewPager viewPager, int pos) {
        this.mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //pager切换监听
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                selectTxtColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                scrollChangeState(state);
            }
        });
        viewPager.setCurrentItem(pos);
    }

    class LineSmoothMoveThread extends Thread {
        @Override
        public void run() {
            super.run();
            if (offsetX != titleViewWidth * (1 / 5) && scroolState == 2) {
                offsetX = offsetX + 2;
            }
        }
    }
}
