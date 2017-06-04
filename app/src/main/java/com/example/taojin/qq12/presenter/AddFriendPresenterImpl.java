package com.example.taojin.qq12.presenter;

import com.example.taojin.qq12.model.User;
import com.example.taojin.qq12.utils.DBUtils;
import com.example.taojin.qq12.utils.ThreadUtil;
import com.example.taojin.qq12.view.AddFriendView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by taojin on 2016/9/10.10:55
 */
public class AddFriendPresenterImpl implements AddFriendPresenter {

    private AddFriendView mAddFriendView;
    public AddFriendPresenterImpl(AddFriendView addFriendView){
        this.mAddFriendView = addFriendView;
    }

    @Override
    public void searchFriend(String keyword) {
        //去Bmob服务器中搜索
        BmobQuery<User> query = new BmobQuery<>();
        //只要username中包含keyword的并且不能搜出自己
        query.addWhereContains("username",keyword);
        query.addWhereNotEqualTo("username", EMClient.getInstance().getCurrentUser());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e==null&&list.size()>0){
                    //成功
                    List<String> contacts = DBUtils.getContacts(EMClient.getInstance().getCurrentUser());

                    mAddFriendView.afterSearch(list,contacts,true);
                }else {
                    //失败 nodata
                    mAddFriendView.afterSearch(null,null,false);
                }
            }
        });


    }

    @Override
    public void addContact(final String username) {
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                //添加好友
                try {
                    EMClient.getInstance().contactManager().addContact(username,"想和你一起玩，赶紧添加我为好友吧。");
                     afterAdd(true,null,username);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    afterAdd(false,e.getMessage(),username);
                }
            }
        });

    }

    private void afterAdd(final boolean success, final String msg, final String username){
        ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAddFriendView.afterAddContact(success, msg, username);
            }
        });
    }

}
