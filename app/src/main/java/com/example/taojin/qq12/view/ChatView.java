package com.example.taojin.qq12.view;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by taojin on 2016/9/10.17:25
 */
public interface ChatView {
    void afterInitData(List<EMMessage> emMessageList,boolean isSmooth);

    void updateChatData(boolean success,String msg,EMMessage emMessage);
}
