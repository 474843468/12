package com.example.taojin.qq12.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taojin.qq12.R;
import com.example.taojin.qq12.adapter.ChatAdapter;
import com.example.taojin.qq12.adapter.TextWatcherAdapter;
import com.example.taojin.qq12.common.BaseActivity;
import com.example.taojin.qq12.presenter.ChatPresenter;
import com.example.taojin.qq12.presenter.ChatPresenterImpl;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity implements TextView.OnEditorActionListener,ChatView {

    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.iv_right)
    ImageView ivRight;
    @InjectView(R.id.chatRecyclerView)
    RecyclerView chatRecyclerView;
    @InjectView(R.id.et_msg)
    EditText etMsg;
    @InjectView(R.id.btn_send)
    Button btnSend;
    private ChatPresenter mChatPresenter;
    private String username;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        if (TextUtils.isEmpty(username)){
            finish();
            showToast("跟鬼聊天呀。");
            return;
        }
        ivLeft.setVisibility(View.VISIBLE);
        mChatPresenter = new ChatPresenterImpl(this);
        tvTitle.setText("与"+ username +"聊天中");
        etMsg.setOnEditorActionListener(this);
        etMsg.addTextChangedListener(new TextWatcherAdapter(){
                @Override
                public void afterTextChanged(Editable s) {
                    super.afterTextChanged(s);
                    if (s.toString().trim().length()==0){
                        btnSend.setEnabled(false);
                    }else {
                        btnSend.setEnabled(true);
                    }
                }
        });
        //初始化RecyclerView，显示历史聊天记录（最多显示20条）
        mChatPresenter.initChatData(username, false);

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage){
        //判断当前接收到的消息是否是发送给当前聊天对象的
        if (username.equals(emMessage.getUserName())){
            //更新RecyclerView
            mChatPresenter.initChatData(username,true);
        }
    }

    @OnClick({R.id.iv_left, R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.btn_send:
                sendMsg();
                break;
        }
    }

    private void sendMsg() {

        String msg = etMsg.getText().toString().trim();
        if (TextUtils.isEmpty(msg)){
            showToast("消息不能为空！");
            return;
        }
        etMsg.getText().clear();
        EMMessage emMessage =  EMMessage.createTxtSendMessage(msg,username);
        mChatPresenter.sendMessage(emMessage);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId== EditorInfo.IME_ACTION_SEND){
            sendMsg();
            return true;
        }
        return false;
    }


    @Override
    public void afterInitData(List<EMMessage> emMessageList,boolean isSmooth) {
        if (chatAdapter==null){
            chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            chatAdapter = new ChatAdapter(emMessageList);
            chatRecyclerView.setAdapter(chatAdapter);
        }else {
            chatAdapter.notifyDataSetChanged();
        }
        if (isSmooth){
            chatRecyclerView.smoothScrollToPosition(emMessageList.size()-1);
        }else {
            chatRecyclerView.scrollToPosition(emMessageList.size()-1);
        }

    }

    @Override
    public void updateChatData(boolean success,String msg,EMMessage emMessage) {
        if (!success){
            showToast(msg);
        }
        chatAdapter.notifyDataSetChanged();
        chatRecyclerView.smoothScrollToPosition(Integer.MAX_VALUE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
