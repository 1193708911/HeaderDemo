package com.ctvit.monic.headerdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by admin on 2018/1/31.
 */

public class HeaderDecoration extends RecyclerView.ItemDecoration {

    private int mDividerHeight = 1;
    private Paint paint;
    private Context context;
    private int mTitleHeight;

    private Paint headerPaint;
    private Paint mTextPaint;

    private Map<Integer, String> keys = new LinkedHashMap<>();
    private float mTextHeight;
    private float mTextBaselineOffset;

    public HeaderDecoration(Context context) {
        initPaint(context);
    }

    private void initPaint(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);

        headerPaint = new Paint();
        headerPaint.setColor(Color.MAGENTA);
        headerPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(18);
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mTextHeight = fm.bottom - fm.top;//计算文字高度

        mTextBaselineOffset = fm.bottom;

        keys.put(0, "标题一");
        keys.put(9, "标题二");
        keys.put(16, "标题三");

        mTitleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, context.getResources().getDisplayMetrics());
    }

    /**
     * 为recycleView  item留出空间
     *
     * @param outRect
     * @param view
     * @param parent
     * @param state
     */


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos = parent.getChildViewHolder(view).getAdapterPosition();
        if (keys.containsKey(pos)) {
            outRect.top = mTitleHeight;
        } else {
            outRect.top = 1;
        }

    }


    /**
     * 绘制分割线主要用于
     *
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);

        for (int i = 0; i < parent.getChildCount(); i++) {
            View itemView = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (keys.containsKey(params.getViewLayoutPosition())) {
                int top = itemView.getTop() - params.topMargin - mTitleHeight;
                int bottom = top + mTitleHeight;
                int left = parent.getPaddingLeft();
                int right = parent.getWidth() - parent.getPaddingRight();
                canvas.drawRect(new Rect(left, top, right, bottom), headerPaint);
                /**
                 * 绘制文字
                 */
                float x = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
                float y = bottom - (mTitleHeight - mTextHeight) / 2 - mTextBaselineOffset;//计算文字baseLine
                canvas.drawText(keys.get(params.getViewLayoutPosition()), x, y, mTextPaint);

            } else {
                int startY = itemView.getTop() - params.topMargin - mDividerHeight;
                canvas.drawLine(0, startY, parent.getWidth(), startY, paint);
            }

        }


    }

    /**
     * 绘制悬浮效果
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        //起初我想先绘制1  3   6三个是否有效果
        int firstVisiblePosition = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
        if (firstVisiblePosition == RecyclerView.NO_POSITION) {
            return;
        }
        String title = getTitle(firstVisiblePosition);
        if (TextUtils.isEmpty(title)) {
            return;
        }

        boolean isFloag = false;
        if (!TextUtils.isEmpty(getTitle(firstVisiblePosition + 1)) && title != getTitle(firstVisiblePosition + 1)) {
            //说明当前已经是该组的最后一个  我们需要进行上推检验
            View child = parent.findViewHolderForAdapterPosition(firstVisiblePosition).itemView;
            if (child.getTop() + child.getMeasuredHeight() < mTitleHeight) {
                //说明开始碰撞，这个时候就开始  平移画布从新绘制
                c.save();
                isFloag = true;
                c.translate(0, child.getTop() + child.getMeasuredHeight() - mTitleHeight);


            }
        }


        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int top = parent.getPaddingTop();
        int bottom = top + mTitleHeight;
        c.drawRect(left, top, right, bottom, paint);
        float x = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
        float y = bottom - (mTitleHeight - mTextHeight) / 2 - mTextBaselineOffset;//计算文字baseLine
        c.drawText(title, x, y, mTextPaint);
        if (isFloag) {
            c.restore();
        }


    }



    public String getTitle(int position) {
        while (position >= 0) {
            String title = keys.get(position);
            if (!TextUtils.isEmpty(title)) {
                return title;
            }
            position--;
        }
        return null;
    }


}
