package com.example.taojin.qq12.view;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.taojin.qq12.MainActivity;
import com.example.taojin.qq12.R;
import com.example.taojin.qq12.common.BaseActivity;
import com.example.taojin.qq12.presenter.SplashPresenter;
import com.example.taojin.qq12.presenter.SplashPresenterImpl;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SplashActivity extends BaseActivity implements SplashView {

    private static final long DURATION = 2000;
    @InjectView(R.id.iv)
    ImageView iv;

    private SplashPresenter mSplashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
        mSplashPresenter = new SplashPresenterImpl(this);
        //判断是否已经登录了，如果登录了，直接进入主界面，如果没有登录，闪2秒，进入登录界面
        mSplashPresenter.isLogined();
    }

    @Override
    public void checkLogined(boolean isLogined) {
        if (isLogined) {
            //跳转到主界面
            startActivity(MainActivity.class, true);
        } else {
            ObjectAnimator.ofFloat(iv, "alpha", 0, 1).setDuration(DURATION).start();
            //2秒之后跳转到登录界面
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(LoginActivity.class, true);
                }
            }, DURATION);
        }
    }
}
