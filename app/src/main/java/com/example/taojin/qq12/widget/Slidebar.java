package com.example.taojin.qq12.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.taojin.qq12.R;
import com.hyphenate.util.DensityUtil;

import java.util.Arrays;

/**
 * Created by taojin on 2016/9/9.15:00
 */
public class Slidebar extends View {

    private static final String[] SECTIONS = {"A","B","C","D","E","F","G","H","I","J","K","L"
    ,"M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    private int measuredWidth;
    private int measuredHeight;
    private Paint paint;
    private float avgHeight;
    private TextView tvFloat;
    private RecyclerView contactRecyclerView;

    public Slidebar(Context context) {
        this(context,null);
    }

    public Slidebar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Slidebar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Slidebar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                showToastAndScroll(event.getY());
                break;
            case MotionEvent.ACTION_UP:
                //隐藏Toast设置背景为透明
                if (tvFloat!=null){
                    tvFloat.setVisibility(GONE);
                }
                setBackgroundColor(Color.TRANSPARENT);
                break;
        }
        return true;
    }

    private void showToastAndScroll(float y) {

        if (tvFloat==null){
            ViewGroup parent = (ViewGroup) getParent();
            tvFloat = (TextView) parent.findViewById(R.id.tv_float);
            contactRecyclerView = (RecyclerView) parent.findViewById(R.id.contact_recyclerView);
        }
        tvFloat.setVisibility(VISIBLE);
        setBackgroundResource(R.drawable.slidebar_bk);
        int index = (int) (y/avgHeight);
        if (index<0){
            index=0;
        }else if (index>SECTIONS.length-1){
            index = SECTIONS.length - 1;
        }
        String slideBarSection = SECTIONS[index];//J
        tvFloat.setText(slideBarSection);
        //定位RecyclerView
//        ContactAdapter contactAdapter = (ContactAdapter) contactRecyclerView.getAdapter();
//        List<String> data = contactAdapter.getData();
//
//        for(int i = 0;i<data.size();i++){
//            if (StringUtils.getInital(data.get(i)).equals(slideBarSection)){
//                contactRecyclerView.smoothScrollToPosition(i);
//                return;
//            }
//        }

        RecyclerView.Adapter adapter = contactRecyclerView.getAdapter();
        if (!(adapter instanceof SectionIndexer)){
            return;
        }

        SectionIndexer sectionIndexer = (SectionIndexer) adapter;
        //获取真实中所有的分区
        String[] sections = (String[]) sectionIndexer.getSections();
        //当前用于点击的是J，然后需要找到J在真实分区中占的脚标
        int sectionIndex = Arrays.binarySearch(sections, slideBarSection);
        if(sectionIndex<0){
            return;
        }
        //根据section的脚标找到条目的脚标(根据分区的index找到分区第一个条目所在的索引)
        int positionForSection = sectionIndexer.getPositionForSection(sectionIndex);
        //根据条目的脚标让RecyclerView定位到该位置
        contactRecyclerView.scrollToPosition(positionForSection);


    }

    private void initView(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#8c8c8c"));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(DensityUtil.sp2px(context,10));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredWidth = getMeasuredWidth();
        measuredHeight = getMeasuredHeight()-getPaddingBottom();
        avgHeight = (measuredHeight +0.f)/ SECTIONS.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x = measuredWidth / 2;
        for(int i=0;i<SECTIONS.length;i++){
            float y = avgHeight * (i+1);
            canvas.drawText(SECTIONS[i],x,y,paint);
        }
    }
}
