package com.example.taojin.qq12.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.taojin.qq12.R;

/**
 * Created by taojin on 2016/9/9.11:44
 */
public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            TextView tvTitle = (TextView) getView().findViewById(R.id.tv_title);
             initTitle(tvTitle);
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        TextView tvTitle = (TextView) getView().findViewById(R.id.tv_title);
//        initTitle(tvTitle);
//    }

    protected abstract void initTitle(TextView tvTitle);
}
