package com.example.taojin.qq12.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.example.taojin.qq12.R;

/**
 * Created by taojin on 2016/9/9.14:54
 */
public class ContactListView extends RelativeLayout {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public ContactListView(Context context) {
            this(context,null);
    }

    public ContactListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ContactListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //绑定布局文件
         LayoutInflater.from(context).inflate(R.layout.contact_list_layout, this, true);
        recyclerView = (RecyclerView) findViewById(R.id.contact_recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ContactListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        //必须做一件事情
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener){
        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    public void setRefreshing(boolean b) {
        if (swipeRefreshLayout!=null){
            swipeRefreshLayout.setRefreshing(b);
        }
    }
}
