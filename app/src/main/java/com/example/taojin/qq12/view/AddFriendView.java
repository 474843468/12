package com.example.taojin.qq12.view;

import com.example.taojin.qq12.model.User;

import java.util.List;

/**
 * Created by taojin on 2016/9/10.10:56
 */
public interface AddFriendView {
    /**
     *
     * @param users 从bmob服务器上搜索出来的用户
     * @param contacts 当前用户的好友
     * @param isSuccess
     */
    void afterSearch(List<User> users, List<String> contacts, boolean isSuccess);
    void afterAddContact(boolean success,String msg,String username);
}
