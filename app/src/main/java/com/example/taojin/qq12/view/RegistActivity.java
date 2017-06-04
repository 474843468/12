package com.example.taojin.qq12.view;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.taojin.qq12.R;
import com.example.taojin.qq12.common.BaseActivity;
import com.example.taojin.qq12.model.User;
import com.example.taojin.qq12.presenter.RegistPresenter;
import com.example.taojin.qq12.presenter.RegistPresenterImpl;
import com.example.taojin.qq12.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by taojin on 2016/9/8.17:02
 */
public class RegistActivity extends BaseActivity implements RegistView, TextView.OnEditorActionListener {
    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.et_pwd)
    EditText etPwd;
    @InjectView(R.id.btn_regist)
    Button btnRegist;

    private RegistPresenter mRegistPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.inject(this);
        etPwd.setOnEditorActionListener(this);
        mRegistPresenter = new RegistPresenterImpl(this);
    }

    @OnClick(R.id.btn_regist)
    public void onClick() {
        regist();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId== EditorInfo.IME_ACTION_GO){
            regist();
            return true;
        }
        return false;
    }

    private void regist() {
        String username = etUsername.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();
        if (!StringUtils.checkUsername(username)){
            showToast("用户名不合法");
            return;
        }else if (!StringUtils.checkPwd(pwd)){
            showToast("密码不合法");
            return;
        }
        mRegistPresenter.regist(username,pwd);
    }

    @Override
    public void showProgressDialog(String msg) {
           showDialog(msg,false);
    }

    @Override
    public void hideProgressDialog() {
        hideDialog();
    }

    @Override
    public void afterRegist(User user, boolean success,String msg) {
        if (success){
            //sp保存数据
            saveUser(user);
            //跳转到登录界面
            startActivity(LoginActivity.class,true);
        }else{
            //弹吐司
            showToast("注册失败："+msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
