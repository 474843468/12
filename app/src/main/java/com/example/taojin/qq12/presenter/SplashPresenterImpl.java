package com.example.taojin.qq12.presenter;

import com.example.taojin.qq12.view.SplashView;
import com.hyphenate.chat.EMClient;

/**
 * Created by taojin on 2016/9/8.14:49
 */
public class SplashPresenterImpl implements SplashPresenter {

    private SplashView mSplashView;
    public SplashPresenterImpl(SplashView splashView){
        this.mSplashView = splashView;
    }
    @Override
    public void isLogined() {
        if ( EMClient.getInstance().isLoggedInBefore()&&EMClient.getInstance().isConnected()){
            //已经登录了
            mSplashView.checkLogined(true);
        }else {
            //没有登录
            mSplashView.checkLogined(false);
        }

    }
}
