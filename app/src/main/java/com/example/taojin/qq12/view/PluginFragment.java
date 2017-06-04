package com.example.taojin.qq12.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taojin.qq12.MainActivity;
import com.example.taojin.qq12.R;
import com.example.taojin.qq12.common.BaseFragment;
import com.example.taojin.qq12.presenter.PluginPresenter;
import com.example.taojin.qq12.presenter.PluginPresenterImpl;
import com.hyphenate.chat.EMClient;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PluginFragment extends BaseFragment implements  PluginView{


    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.iv_right)
    ImageView ivRight;
    @InjectView(R.id.btn_login)
    Button btnLogin;

    private PluginPresenter mPluginPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plugin, container, false);
        ButterKnife.inject(this, view);
        mPluginPresenter = new PluginPresenterImpl(this);
        String currentUser = EMClient.getInstance().getCurrentUser();
        btnLogin.setText("退（"+currentUser+"）出");
        return view;
    }

    @Override
    protected void initTitle(TextView tvTitle) {
        tvTitle.setText("动态");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.btn_login)
    public void onClick() {
        mPluginPresenter.logout();
    }

    @Override
    public void afterLogout(boolean success, String msg) {
        MainActivity activity = (MainActivity) getActivity();
        activity.startActivity(LoginActivity.class,true);
        if (!success){
           activity.showToast(msg);
        }
    }
}
