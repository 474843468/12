package com.example.taojin.qq12;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;

import com.example.taojin.qq12.adapter.EMMessageListenerAdapter;
import com.example.taojin.qq12.common.BaseActivity;
import com.example.taojin.qq12.utils.DBUtils;
import com.example.taojin.qq12.utils.ToastUtil;
import com.example.taojin.qq12.view.ChatActivity;
import com.example.taojin.qq12.view.LoginActivity;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;

/**
 * Created by taojin on 2016/9/8.15:37
 */
public class QQApplication extends Application {

    private List<BaseActivity> activityList = new ArrayList<>();
    private SoundPool soundPool;
    private int duan;
    private int yulu;
    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        DBUtils.init(this);
        initSoundpool();
        initHuanxin();
        initBmob();
        initListener();
    }

    public void addActivity(BaseActivity activity){
        if (!activityList.contains(activity)){
            activityList.add(activity);
        }
    }
    public void removeActivity(BaseActivity activity){
        activityList.remove(activity);
    }


    private void initSoundpool() {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        duan = soundPool.load(this, R.raw.duan, 1);
        yulu = soundPool.load(this, R.raw.yulu, 1);
    }

    private void initListener() {
        //监听新的消息的到来
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListenerAdapter() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                super.onMessageReceived(list);

                //发出声音通知（如果应用时在后台运行则播放长声音，如果是在前台运行就播放短声音）
                if (isRuninBackground()) {
                    soundPool.play(yulu,1,1,0,0,1);
                    //显示通知栏
                    showNotification(list.get(0));
                } else {
                    soundPool.play(duan,1,1,0,0,1);
                }
                //发布事件
                EventBus.getDefault().post(list.get(0));
            }
        });
        //监听连接状态
        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
            @Override
            public void onConnected() {
            }

            @Override
            public void onDisconnected(int i) {
               if (i==EMError.USER_LOGIN_ANOTHER_DEVICE){

                   for(BaseActivity activity : activityList){
                       activity.finish();
                   }
                   activityList.clear();

                   Intent intent = new Intent(QQApplication.this, LoginActivity.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(intent);
                   ToastUtil.showToast(getApplicationContext(),"您的账号已经在其他设备登录了。");
               }
            }
        });
    }

    private void showNotification(EMMessage emMessage) {
        if (notificationManager==null){
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }

        String msg = "";
        EMMessageBody body = emMessage.getBody();
        if (body instanceof EMTextMessageBody){
            EMTextMessageBody emTextMessageBody = (EMTextMessageBody) body;
            msg = emTextMessageBody.getMessage();
        }

        Intent mainIntent = new Intent(this,MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Intent chatIntent = new Intent(this, ChatActivity.class);
        chatIntent.putExtra("username",emMessage.getUserName());

        Intent[] intents = {mainIntent,chatIntent};

        PendingIntent pendingIntent = PendingIntent.getActivities(this,1,intents,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.avatar3))
                .setSmallIcon(R.mipmap.contact_selected_2)
                .setContentText(msg)
                .setContentTitle("您有一条新消息")
                .setContentInfo("来自"+emMessage.getUserName())
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.notify(1,notification);
    }

    private boolean isRuninBackground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(100);
        //获取到的是最上层的任务栈
        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
        //是任务栈中最上面的Activity
        return  !runningTaskInfo.topActivity.getPackageName().equals(getPackageName());

    }


    private void initBmob() {
        Bmob.initialize(this, "8e36082282d21093be1e5a3e892b8014");
    }

    private void initHuanxin() {
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
//初始化

        int pid = android.os.Process.myPid();//进程id
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null || !processAppName.equalsIgnoreCase(getPackageName())) {
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        EMClient.getInstance().init(this, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

}
