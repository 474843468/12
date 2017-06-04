package com.example.taojin.qq12.view;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.taojin.qq12.MainActivity;
import com.example.taojin.qq12.R;
import com.example.taojin.qq12.common.BaseActivity;
import com.example.taojin.qq12.model.User;
import com.example.taojin.qq12.presenter.LoginPresenter;
import com.example.taojin.qq12.presenter.LoginPresenterImpl;
import com.example.taojin.qq12.utils.StringUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginView, TextView.OnEditorActionListener {

    private static final int REQUEST_PERMISSION_WRITE_SDCARD = 1;
    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.et_pwd)
    EditText etPwd;
    @InjectView(R.id.btn_login)
    Button btnLogin;
    @InjectView(R.id.tv_newuser)
    TextView tvNewuser;
    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        mLoginPresenter = new LoginPresenterImpl(this);
        etPwd.setOnEditorActionListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //数据回显
        User user = getUser();
        etUsername.setText(user.getUsername());
        etPwd.setText(user.getPassword2());
    }

    @OnClick({R.id.btn_login, R.id.tv_newuser})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_newuser:
                startActivity(RegistActivity.class,false);
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId== EditorInfo.IME_ACTION_GO){
            login();
            return true;
        }
        return  false;
    }

    private void login() {
        //3到20位，首字母必须是字母
        String username = etUsername.getText().toString().trim();
        //3到20位的数字
        String pwd = etPwd.getText().toString().trim();
        if (!StringUtils.checkUsername(username)){
            showToast("用户名不合法。3到20位，首字母必须是字母");
            return;
        }else if (!StringUtils.checkPwd(pwd)){
            showToast("密码不合法。3到20位的数字");
            return;
        }
        //检查权限

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PermissionChecker.PERMISSION_GRANTED){
            //没有被授权//请求用户给权限
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION_WRITE_SDCARD);
            return;
        }else {
            mLoginPresenter.login(username,pwd);
        }

    }
    //获取到权限，执行代码逻辑
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_PERMISSION_WRITE_SDCARD){
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)&&grantResults[0]==PermissionChecker.PERMISSION_GRANTED){
               login();
            }else {
                showToast("您把我拒绝了，不让你用了");
            }
        }
    }

    @Override
    public void afterLogin(User user, boolean isSuccess, String msg) {
        if (isSuccess){
            //保存usr
            saveUser(user);
            //跳转到主界面
            startActivity(MainActivity.class,true);
        }else {
            //弹吐司
            showToast(msg);
        }
    }

    @Override
    public void showProgressDialog(String msg) {
            showDialog(msg,false);
    }

    @Override
    public void hideProgressDialog() {
        hideDialog();
    }
}
