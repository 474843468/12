package com.example.taojin.qq12.presenter;

import com.example.taojin.qq12.adapter.EMCallBackAdapter;
import com.example.taojin.qq12.model.User;
import com.example.taojin.qq12.utils.ThreadUtil;
import com.example.taojin.qq12.view.LoginView;
import com.hyphenate.chat.EMClient;

/**
 * Created by taojin on 2016/9/8.16:37
 */
public class LoginPresenterImpl implements LoginPresenter {

    private LoginView mLoginView;
    public LoginPresenterImpl(LoginView loginView){
        this.mLoginView = loginView;
    }

    @Override
    public void login(final String username, final String pwd) {
        //显示进度条对话库
        mLoginView.showProgressDialog("正在登录");
//        mLoginView.hashCode()
        //隐藏进度条对话框
        EMClient.getInstance().login(username, pwd, new EMCallBackAdapter() {
            @Override
            public void onSuccess() {
                //从本地数据库加载会话对象到内存中
                EMClient.getInstance().chatManager().loadAllConversations();
                hideDialog(new User(username,pwd),true,null);
            }

            @Override
            public void onError(int i, String s) {
                hideDialog(new User(username,pwd),false,s);
            }

        });
    }

    private void hideDialog(final User user, final boolean isSuccess, final String msg) {
        ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoginView.afterLogin(user,isSuccess,msg);
                mLoginView.hideProgressDialog();
            }
        });
    }
}
