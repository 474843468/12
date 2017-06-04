package com.example.taojin.qq12.view;

import com.example.taojin.qq12.model.User;

/**
 * Created by taojin on 2016/9/8.17:03
 */
public interface RegistView extends BaseView{
    void afterRegist(User user,boolean success,String msg);
}
