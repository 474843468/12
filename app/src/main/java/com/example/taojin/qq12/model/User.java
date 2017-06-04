package com.example.taojin.qq12.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by taojin on 2016/9/8.17:25
 */
public class User  extends BmobUser{

    public User(){};

    public User(String username,String pwd){
        setUsername(username);
        setPassword(pwd);
    }

    private String password2;
    @Override
    public void setPassword(String password) {
        super.setPassword(password);
        this.password2 = password;
    }

    public String getPassword2(){
        return password2;
    }
}
