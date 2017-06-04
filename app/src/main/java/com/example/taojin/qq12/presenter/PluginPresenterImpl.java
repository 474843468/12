package com.example.taojin.qq12.presenter;

import com.example.taojin.qq12.adapter.EMCallBackAdapter;
import com.example.taojin.qq12.utils.ThreadUtil;
import com.example.taojin.qq12.view.PluginView;
import com.hyphenate.chat.EMClient;

/**
 * Created by taojin on 2016/9/9.14:37
 */
public class PluginPresenterImpl implements PluginPresenter {
    private PluginView mPluginView;
    public PluginPresenterImpl(PluginView pluginView){
        this.mPluginView = pluginView;
    }
    @Override
    public void logout() {
        //解绑推送
        EMClient.getInstance().logout(true,new EMCallBackAdapter(){
            @Override
            public void onSuccess() {
                super.onSuccess();
                gotoLogin(true,null);
            }

            @Override
            public void onError(int i, String s) {
                super.onError(i, s);
                gotoLogin(false,s);
            }
        });
    }

    private void gotoLogin(final boolean success, final String msg) {
        ThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPluginView.afterLogout(success,msg);
            }
        });
    }
}
