package com.example.taojin.qq12.presenter;

import com.example.taojin.qq12.view.ConversationView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by taojin on 2016/9/12.16:42
 */
public class ConversationPresenterImpl implements ConversationPresenter {
    private List<EMConversation> emConversationList = new ArrayList<>();

    private ConversationView mConversationView;
    public ConversationPresenterImpl(ConversationView conversationView){
        this.mConversationView = conversationView;
    }

    @Override
    public void initConversation() {
        //获取所有的会话
        Map<String, EMConversation> allConversations = EMClient.getInstance().chatManager().getAllConversations();
        emConversationList.clear();
        emConversationList.addAll(allConversations.values());
        Collections.sort(emConversationList, new Comparator<EMConversation>() {
            @Override
            public int compare(EMConversation lhs, EMConversation rhs) {
                return (int) (rhs.getLastMessage().getMsgTime()-lhs.getLastMessage().getMsgTime());
            }
        });
        mConversationView.initData(emConversationList);




    }
}
