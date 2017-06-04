package com.example.taojin.qq12.view;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taojin.qq12.MainActivity;
import com.example.taojin.qq12.R;
import com.example.taojin.qq12.adapter.ContactAdapter;
import com.example.taojin.qq12.common.BaseFragment;
import com.example.taojin.qq12.event.ContactUpdateEvent;
import com.example.taojin.qq12.presenter.ContactsPresenter;
import com.example.taojin.qq12.presenter.ContactsPresenterImpl;
import com.example.taojin.qq12.utils.ToastUtil;
import com.example.taojin.qq12.widget.ContactListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends BaseFragment implements ContactsView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, ContactAdapter.OnItemClickListener {


    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.iv_right)
    ImageView ivRight;
    @InjectView(R.id.contactListView)
    ContactListView contactListView;

    private ContactsPresenter mContactsPresenter;
    private ContactAdapter contactAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.inject(this, view);
        ivRight.setVisibility(View.VISIBLE);
        ivRight.setOnClickListener(this);
        mContactsPresenter = new ContactsPresenterImpl(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contactListView.setOnRefreshListener(this);
        //初始化联系人
        mContactsPresenter.initContacts();
        //订阅事件
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ContactUpdateEvent contactUpdateEvent){
        mContactsPresenter.initContacts();
    }


    @Override
    protected void initTitle(TextView tvTitle) {
        tvTitle.setText("联系人");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        //取消订阅事件
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showContacts(List<String> contacts) {
        contactAdapter = new ContactAdapter(contacts);
        contactListView.setAdapter(contactAdapter);
        contactAdapter.setOnItemClickListener(this);

    }

    @Override
    public void updateContacts(boolean success) {
        contactListView.setRefreshing(false);
        if (success){
             contactAdapter.notifyDataSetChanged();
        }else {
            ToastUtil.showToast(getActivity(),"通讯录同步失败");
        }
    }

    @Override
    public void afterContact(boolean isSuccess, String username) {
        if (isSuccess){
            ToastUtil.showToast(getActivity(),"删除好友成功，还可以再添加为好友");
        }else {
            ToastUtil.showToast(getActivity(),"删除失败，请稍后再删除："+username);
        }
    }

    @Override
    public void onRefresh() {
        //更新数据
        mContactsPresenter.updateContact();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_right:
                MainActivity activity = (MainActivity) getActivity();
                activity.startActivity(AddFriendActivity.class,false);
                break;
        }
    }

    @Override
    public void onItemClick(String username) {
        MainActivity activity = (MainActivity) getActivity();
        Intent intent = new Intent(getActivity(),ChatActivity.class);
        intent.putExtra("username",username);
        activity.startActivity(intent);
    }

    @Override
    public void onItemLongClick(final String username) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setMessage("您确定和"+username+"友尽了吗？")
                .setNegativeButton("友尽", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mContactsPresenter.deleteContact(username);
                    }
                })
                .setPositiveButton("再续前缘", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .create();

        alertDialog.show();
    }
}
