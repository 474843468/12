package com.example.taojin.qq12.presenter;

import com.example.taojin.qq12.model.User;
import com.example.taojin.qq12.utils.ThreadUtil;
import com.example.taojin.qq12.view.RegistView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by taojin on 2016/9/8.17:06
 */
public class RegistPresenterImpl implements RegistPresenter {
    private RegistView registView;
    public RegistPresenterImpl(RegistView registView){
        this.registView = registView;
    }
    @Override
    public void regist(final String username, final String pwd) {
        //显示进度条对话框
        registView.showProgressDialog("正在注册");
        //隐藏进度条对话框
        User user = new User();
        user.setPassword(pwd);
        user.setUsername(username);
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(final User user, BmobException e) {
                if (e==null){
                    ThreadUtil.runOnSubThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().createAccount(username, pwd);
                                //注册成功
                                ThreadUtil.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        registView.hideProgressDialog();
                                        registView.afterRegist(user,true,null);
                                    }
                                });

                            } catch (final HyphenateException e1) {
                                e1.printStackTrace();
                                //注册失败了
                                user.delete();
                                ThreadUtil.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        registView.hideProgressDialog();
                                        registView.afterRegist(user,false,e1.getMessage());
                                    }
                                });
                            }
                        }
                    });
                }else {
                    registView.hideProgressDialog();
                    registView.afterRegist(user,false,e.getMessage());
                }
            }
        });
    }
}
