package com.example.taojin.qq12.view;

import java.util.List;

/**
 * Created by taojin on 2016/9/9.15:58
 */
public interface ContactsView {
    void showContacts(List<String> contacts);

    void updateContacts(boolean success);

    void afterContact(boolean isSuccess,String username);
}
