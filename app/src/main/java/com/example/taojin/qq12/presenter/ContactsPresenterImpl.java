package com.example.taojin.qq12.presenter;

import com.example.taojin.qq12.utils.DBUtils;
import com.example.taojin.qq12.utils.ThreadUtil;
import com.example.taojin.qq12.view.ContactsView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by taojin on 2016/9/9.15:58
 */
public class ContactsPresenterImpl implements ContactsPresenter{

    private ContactsView mContactsView;
    public ContactsPresenterImpl(ContactsView contactsView){
        this.mContactsView = contactsView;
    }
    private List<String> contactsList = new ArrayList<>();

    @Override
    public void initContacts() {
        //首先走本地缓存
        final List<String> contacts = DBUtils.getContacts(EMClient.getInstance().getCurrentUser());
        contactsList.clear();
        contactsList.addAll(contacts);
        mContactsView.showContacts(contactsList);
        //然后走网络
        update();


    }

    @Override
    public void updateContact() {
       update();
    }

    @Override
    public void deleteContact(final String username) {
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().contactManager().deleteContact(username);
                    //成功
                    afterDelete(true,username);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    //失败
                    afterDelete(false,e.getMessage());
                }
            }
        });
    }

    private void afterDelete(final boolean isSuccess, final String message) {
        ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContactsView.afterContact(isSuccess,message);
            }
        });
    }

    private void update(){
        ThreadUtil.runOnSubThread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> allContactsFromServer = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    Collections.sort(allContactsFromServer, new Comparator<String>() {
                        @Override
                        public int compare(String lhs, String rhs) {
                            return lhs.compareTo(rhs);
                        }
                    });
                    contactsList.clear();
                    contactsList.addAll(allContactsFromServer);
                    //更新本地缓存
                    DBUtils.saveContacts(EMClient.getInstance().getCurrentUser(),contactsList);

                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //通知界面更新
                            mContactsView.updateContacts(true);
                        }
                    });

                } catch (HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mContactsView.updateContacts(false);
                        }
                    });
                }
            }
        });
    }
}
