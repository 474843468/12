package com.example.taojin.qq12;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.taojin.qq12.adapter.TabSelectedListenerAdapter;
import com.example.taojin.qq12.common.BaseActivity;
import com.example.taojin.qq12.common.BaseFragment;
import com.example.taojin.qq12.event.ContactUpdateEvent;
import com.example.taojin.qq12.utils.FragmentFactory;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity implements EMContactListener {

    @InjectView(R.id.fl_content)
    FrameLayout flContent;
    @InjectView(R.id.bottom_navigation_bar)
    BottomNavigationBar bottomNavigationBar;
    private BadgeItem badgeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initListener();
        initFragment();
        initBottomBar();
        EventBus.getDefault().register(this);
    }

    private void initListener() {
        EMClient.getInstance().contactManager().setContactListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().contactManager().removeContactListener(this);
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EMMessage emMessage){
       updateUnreadMsgCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUnreadMsgCount();
    }

    private void updateUnreadMsgCount() {
        //获取所有的未读消息
        int unreadMsgsCount = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        if (unreadMsgsCount>99){
            badgeItem.setText("99+");
            badgeItem.show(true);
        }else if (unreadMsgsCount>0){
            badgeItem.setText(unreadMsgsCount+"");
            badgeItem.show(true);
        }else {
            badgeItem.hide(true);
        }
    }


    private void initBottomBar() {
        badgeItem = new BadgeItem();
        badgeItem
                .hide()
                .setGravity(Gravity.RIGHT)
                .setBackgroundColor(Color.RED)
                .setTextColor(Color.WHITE)
                .setHideOnSelect(false)
                .setAnimationDuration(100)
                .show();

        bottomNavigationBar
                .setActiveColor("#00ACFF").setInActiveColor("#ABADBB")
                .addItem(new BottomNavigationItem(R.mipmap.conversation_selected_2,"消息").setBadgeItem(badgeItem))
                .addItem(new BottomNavigationItem(R.mipmap.contact_selected_2,"联系人"))
                .addItem(new BottomNavigationItem(R.mipmap.plugin_selected_2,"动态"))
                .setFirstSelectedPosition(0)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(new TabSelectedListenerAdapter() {
            @Override
            public void onTabSelected(int position) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                //隐藏其他
                fragmentTransaction.hide(FragmentFactory.getFragment(0));
                fragmentTransaction.hide(FragmentFactory.getFragment(1));
                fragmentTransaction.hide(FragmentFactory.getFragment(2));

                //显示当前
                //判断是否已经添加了，如果添加了，则显示，否则先添加再显示
                BaseFragment fragment = FragmentFactory.getFragment(position);
                if (!fragment.isAdded()){
                    fragmentTransaction.add(R.id.fl_content,fragment,position+"");
                }
                fragmentTransaction.show(fragment);

                fragmentTransaction.commit();

            }

        });

    }

    private void initFragment() {
        BaseFragment fragment = FragmentFactory.getFragment(0);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content,fragment,"0").commit();
    }

    @Override
    public void onContactAdded(String s) {
        //用EventBus发布事件
        EventBus.getDefault().post(new ContactUpdateEvent(true,s));
    }

    @Override
    public void onContactDeleted(String s) {
        EventBus.getDefault().post(new ContactUpdateEvent(false,s));
    }

    @Override
    public void onContactInvited(final String s, final String s1) {
        //同意或者拒绝
        try {
            EMClient.getInstance().contactManager().acceptInvitation(s);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
//        ThreadUtil.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                showToast("收到"+s+"发送过来的邀请："+s1);
//            }
//        });
        showToast("收到"+s+"发送过来的邀请："+s1);

    }

    @Override
    public void onContactAgreed(String s) {

    }

    @Override
    public void onContactRefused(String s) {

    }
}
