package com.example.taojin.qq12.view;

import com.example.taojin.qq12.model.User;

/**
 * Created by taojin on 2016/9/8.16:35
 */
public interface LoginView extends BaseView{
    void afterLogin(User user,boolean isSuccess,String msg);

}
