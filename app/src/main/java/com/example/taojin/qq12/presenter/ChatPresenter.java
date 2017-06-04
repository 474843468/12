package com.example.taojin.qq12.presenter;

import com.hyphenate.chat.EMMessage;

/**
 * Created by taojin on 2016/9/10.17:25
 */
public interface ChatPresenter {
    void sendMessage(EMMessage emMessage);

    void initChatData(String username, boolean isSmooth);
}
