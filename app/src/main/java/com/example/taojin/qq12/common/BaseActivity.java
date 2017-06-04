package com.example.taojin.qq12.common;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.taojin.qq12.QQApplication;
import com.example.taojin.qq12.model.User;
import com.example.taojin.qq12.utils.ToastUtil;

/**
 * Created by taojin on 2016/9/8.15:59
 */
public class BaseActivity extends AppCompatActivity {
    protected Handler mHandler = new Handler();
    private ProgressDialog mProgressDialog;
    private SharedPreferences mSharedPreferences;
    private static final String USERNAME_KEY = "username";
    private static final String PWD_KEY = "pwd";
    private QQApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(this);
        mSharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        application = (QQApplication) getApplication();
        application.addActivity(this);
    }

    public void saveUser(User user){
        mSharedPreferences.edit()
                .putString(USERNAME_KEY,user.getUsername())
                .putString(PWD_KEY,user.getPassword2())
                .commit();
    }

    public User getUser(){
        String username = mSharedPreferences.getString(USERNAME_KEY, "");
        String pwd = mSharedPreferences.getString(PWD_KEY, "");
        User user = new User(username,pwd);
        return user;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProgressDialog.dismiss();
        application.removeActivity(this);
    }

    public void showDialog(String msg,boolean isCancelable ){
        mProgressDialog.setCancelable(isCancelable);
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    public void hideDialog(){
        if (mProgressDialog.isShowing()){
            mProgressDialog.hide();
        }
    }

    public void  startActivity(Class clazz, boolean isFinish){
        startActivity(new Intent(this,clazz));
        if (isFinish){
            finish();
        }
    }

    public void showToast(String msg){
        ToastUtil.showToast(this,msg);
    }

}
