package com.example.taojin.qq12.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taojin.qq12.R;
import com.example.taojin.qq12.adapter.SearchAdapter;
import com.example.taojin.qq12.common.BaseActivity;
import com.example.taojin.qq12.model.User;
import com.example.taojin.qq12.presenter.AddFriendPresenter;
import com.example.taojin.qq12.presenter.AddFriendPresenterImpl;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddFriendActivity extends BaseActivity implements AddFriendView,TextView.OnEditorActionListener {

    private AddFriendPresenter mAddFriendPresenter;
    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.iv_right)
    ImageView ivRight;
    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.iv_search)
    ImageView ivSearch;
    @InjectView(R.id.iv_nodata)
    ImageView ivNodata;
    @InjectView(R.id.searchRecyclerView)
    RecyclerView searchRecyclerView;
    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.inject(this);
        searchRecyclerView.setVisibility(View.GONE);
        tvTitle.setText("搜索");
        etUsername.setOnEditorActionListener(this);
        mAddFriendPresenter = new AddFriendPresenterImpl(this);
        //获取输入法管理器
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    }

    @OnClick(R.id.iv_search)
    public void onClick() {
        search();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId== EditorInfo.IME_ACTION_SEARCH){
            search();
            return true;
        }
        return false;
    }

    private void search() {
        String username = etUsername.getText().toString().trim();
        if (TextUtils.isEmpty(username)){
            showToast("你让我搜鬼呀！");
            return;
        }
        mAddFriendPresenter.searchFriend(username);
        //隐藏输入法
        if (inputMethodManager.isActive()){
            //切换输入法的显示与隐藏状态
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    @Override
    public void afterSearch(List<User> users, List<String> contacts, boolean isSuccess) {
        if (isSuccess){
            ivNodata.setVisibility(View.GONE);
            searchRecyclerView.setVisibility(View.VISIBLE);
            searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            SearchAdapter searchAdapter = new SearchAdapter(users, contacts);
            searchAdapter.setOnAddFriendClickListener(new SearchAdapter.OnAddFriendClickListener() {
                @Override
                public void onClick(String username) {
                    mAddFriendPresenter.addContact(username);
                }
            });
            searchRecyclerView.setAdapter(searchAdapter);
            showToast("搜索完毕");
        }else {
            ivNodata.setVisibility(View.VISIBLE);
            searchRecyclerView.setVisibility(View.GONE);
            showToast("没有搜索到结果");
        }
    }

    @Override
    public void afterAddContact(boolean success, String msg, String username) {
        if (success){
            showToast("添加"+username+"请求发送成功");
        }else {
            showToast("添加"+username+"请求发送失败："+msg);
        }

    }

}
