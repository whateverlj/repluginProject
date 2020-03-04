package com.example.myplugindemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

public class MyLayoutManager  extends RecyclerView.LayoutManager {
    /**
     * 一次完整的聚焦滑动所需要的移动距离
     */
    private float onceCompleteScrollLength = -1;

    /**
     * 第一个子view的偏移量
     */
    private float firstChildCompleteScrollLength = -1;

    /**
     * 屏幕可见第一个view的position
     */
    private int mFirstVisiPos;

    /**
     * 屏幕可见的最后一个view的position
     */
    private int mLastVisiPos;

    /**
     * 水平方向累计偏移量
     */
    private long mHorizontalOffset;
    private int childWidth = 0;
    /**
     * 普通view之间的margin
     */
    private float normalViewGap = 0;

    public MyLayoutManager(Context context, int gap) {
        normalViewGap = dp2px(context, gap);
    }

    public MyLayoutManager(Context context) {
        this(context, 0);
    }

    public static float dp2px(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        detachAndScrapAttachedViews(recycler);
        fill(recycler, state, 0);
    }
    private int fill(RecyclerView.Recycler recycler, RecyclerView.State state, int dx) {
        int resultDelta = dx;
        resultDelta = fillHorizontalLeft(recycler, state, dx);
//        recycleChildren(recycler);
        return resultDelta;
    }

    private int fillHorizontalLeft(RecyclerView.Recycler recycler, RecyclerView.State state, int dx) {
        //----------------1、边界检测-----------------
        if (dx < 0) {
            // 已到达左边界
            if (mHorizontalOffset < 0) {
                mHorizontalOffset = dx = 0;
            }
            //向上滚动
            //现在顶部填充
            View  firstView = getChildAt(0);
            int layoutPostion = getPosition(firstView);

            if (firstView.getLeft() >= 0 ){
                View scrap ;
                if (layoutPostion == 0){
                    scrap = recycler.getViewForPosition(getItemCount()-1);
                }else {
                    scrap = recycler.getViewForPosition(layoutPostion -1);
                }
                addView(scrap,0);
                measureChildWithMargins(scrap,0,0);
//                int width = getDecoratedMeasuredWidth(scrap);
//                int height = getDecoratedMeasuredHeight(scrap);
//                layoutDecorated(scrap,0,firstView.getTop() - height,width,firstView.getTop());
            }
        }

        if (dx > 0) {
            if (mHorizontalOffset >= getMaxOffset()) {
                // 因为在因为scrollHorizontallyBy里加了一次dx，现在减回去
                // mHorizontalOffset -= dx;
                mHorizontalOffset = (long) getMaxOffset();
                dx = 0;
            }
            //先在底部填充
            View  lastView = getChildAt(getChildCount() -1);
            int lastPos = getPosition(lastView);
            if (lastView.getRight() -  dx < getWidth()){
                View scrap;
                if (lastPos == getItemCount() -1){
                    scrap = recycler.getViewForPosition(0);
                }else {
                    scrap = recycler.getViewForPosition(lastPos+1);
                }
                addView(scrap);
                measureChildWithMargins(scrap,0,0);
//                int width = getDecoratedMeasuredWidth(scrap);
//                int height = getDecoratedMeasuredHeight(scrap);
//                layoutDecorated(scrap,0,lastView.getBottom(),width,lastView.getBottom()+height);
            }
        }

        // 分离全部的view，加入到临时缓存
        detachAndScrapAttachedViews(recycler);

        float startX = 0;
        float fraction = 0f;
        boolean isChildLayoutLeft = true;

        View tempView = null;
        int tempPosition = -1;

        if (onceCompleteScrollLength == -1) {
            // 因为mFirstVisiPos在下面可能被改变，所以用tempPosition暂存一下
            tempPosition = mFirstVisiPos;
            tempView = recycler.getViewForPosition(tempPosition);
            measureChildWithMargins(tempView, 0, 0);
            childWidth = getDecoratedMeasurementHorizontal(tempView);
        }

        // 修正第一个可见view mFirstVisiPos 已经滑动了多少个完整的onceCompleteScrollLength就代表滑动了多少个item
        firstChildCompleteScrollLength = getWidth() / 2 + childWidth / 2;
        if (mHorizontalOffset >= firstChildCompleteScrollLength) {
            startX = normalViewGap;
            onceCompleteScrollLength = childWidth + normalViewGap;
            mFirstVisiPos = (int) Math.floor(Math.abs(mHorizontalOffset - firstChildCompleteScrollLength) / onceCompleteScrollLength) + 1;
            fraction = (Math.abs(mHorizontalOffset - firstChildCompleteScrollLength) % onceCompleteScrollLength) / (onceCompleteScrollLength * 1.0f);
        } else {
            mFirstVisiPos = 0;
            startX = getMinOffset();
            onceCompleteScrollLength = firstChildCompleteScrollLength;
            fraction = (Math.abs(mHorizontalOffset) % onceCompleteScrollLength) / (onceCompleteScrollLength * 1.0f);
        }

        // 临时将mLastVisiPos赋值为getItemCount() - 1，放心，下面遍历时会判断view是否已溢出屏幕，并及时修正该值并结束布局
        mLastVisiPos = getItemCount() - 1;

        float normalViewOffset = onceCompleteScrollLength * fraction;
        boolean isNormalViewOffsetSetted = false;

        //----------------3、开始布局-----------------
        for (int i = mFirstVisiPos; i <= mLastVisiPos; i++) {
            View item;
            if (i == tempPosition && tempView != null) {
                // 如果初始化数据时已经取了一个临时view
                item = tempView;
            } else {
                item = recycler.getViewForPosition(i);
            }

            int focusPosition = (int) (Math.abs(mHorizontalOffset) / (childWidth + normalViewGap));
            if (i <= focusPosition) {
                addView(item);
            } else {
                addView(item, 0);
            }
            measureChildWithMargins(item, 0, 0);

            if (!isNormalViewOffsetSetted) {
                startX -= normalViewOffset;
                isNormalViewOffsetSetted = true;
            }

            int l, t, r, b;
            l = (int)startX;
            t = getPaddingTop();
            r = l + getDecoratedMeasurementHorizontal(item) ;
            b = t + getDecoratedMeasurementVertical(item);

            // 缩放子view
            final float minScale = 0.5f;
            float currentScale = 0f;
            final int childCenterX = (r + l) / 2;
            final int parentCenterX = getWidth() / 2;
            isChildLayoutLeft = childCenterX <= parentCenterX;
            if (isChildLayoutLeft) {
                final float fractionScale = (parentCenterX - childCenterX) / (parentCenterX * 1.0f);
                currentScale = 1.0f - (1.0f - minScale) * fractionScale;
            } else {
                final float fractionScale = (childCenterX - parentCenterX) / (parentCenterX * 1.0f);
                currentScale = 1.0f - (1.0f - minScale) * fractionScale;
            }
//            item.setScaleX(currentScale);
            item.setScaleY(currentScale);
            // item.setAlpha(currentScale);

            layoutDecoratedWithMargins(item, l, t, r, b);

            startX += (childWidth + normalViewGap);

            if (startX > getWidth() - getPaddingRight()) {
                mLastVisiPos = i;
                break;
            }
        }

        return dx;
    }
    /**
     * 最大偏移量
     *
     * @return
     */
    private float getMaxOffset() {
        if (childWidth == 0 || getItemCount() == 0) return 0;
        // getWidth() / 2 + childWidth / 2 +
        return (childWidth + normalViewGap) * (getItemCount() - 1);
    }

    /**
     * 获取最小的偏移量
     *
     * @return
     */
    private float getMinOffset() {
        if (childWidth == 0) return 0;
        return 0;
    }

    /**
     * 获取某个childView在水平方向所占的空间，将margin考虑进去
     *
     * @param view
     * @return
     */
    public int getDecoratedMeasurementHorizontal(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedMeasuredWidth(view) + params.leftMargin
                + params.rightMargin;
    }

    /**
     * 获取某个childView在竖直方向所占的空间,将margin考虑进去
     *
     * @param view
     * @return
     */
    public int getDecoratedMeasurementVertical(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedMeasuredHeight(view) + params.topMargin
                + params.bottomMargin;
    }
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        // 手指从右向左滑动，dx > 0; 手指从左向右滑动，dx < 0;
        // 位移0、没有子View 当然不移动
        if (dx == 0 || getChildCount() == 0) {
            return 0;
        }

        float realDx = dx / 1.0f;
        if (Math.abs(realDx) < 0.00000001f) {
            return 0;
        }

        mHorizontalOffset += dx;

        dx = fill(recycler, state, dx);

        return dx;
    }
}
