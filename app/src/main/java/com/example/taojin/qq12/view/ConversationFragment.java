package com.example.taojin.qq12.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taojin.qq12.R;
import com.example.taojin.qq12.adapter.ConversationAdapter;
import com.example.taojin.qq12.common.BaseFragment;
import com.example.taojin.qq12.presenter.ConversationPresenter;
import com.example.taojin.qq12.presenter.ConversationPresenterImpl;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversationFragment extends BaseFragment implements ConversationView, ConversationAdapter.OnConversationItemClickListener {


    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.iv_right)
    ImageView ivRight;
    @InjectView(R.id.conversationRecyclerView)
    RecyclerView conversationRecyclerView;
    private ConversationPresenter mConversationPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        ButterKnife.inject(this, view);
        mConversationPresenter = new ConversationPresenterImpl(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage){
        mConversationPresenter.initConversation();
    }

    @Override
    public void onResume() {
        super.onResume();
        mConversationPresenter.initConversation();
    }

    @Override
    protected void initTitle(TextView tvTitle) {
        tvTitle.setText("消息");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initData(List<EMConversation> emConversationList) {
        conversationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ConversationAdapter conversationAdapter = new ConversationAdapter(emConversationList);
        conversationAdapter.setOnConversationItemClickListener(this);
        conversationRecyclerView.setAdapter(conversationAdapter);

    }

    @Override
    public void onItemClick(String username) {
        Intent intent = new Intent(getActivity(),ChatActivity.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }
}
