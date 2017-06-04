package com.example.taojin.qq12.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by taojin on 2016/9/8.16:03
 */
public class ToastUtil {

    private static Toast sToast;
    private static Handler handler = new Handler(Looper.getMainLooper());

    public static void showToast(final Context context, final String msg) {
//        if (sToast==null){
//            sToast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
//        }
//        sToast.setText(msg);
//        sToast.show();


        if (sToast == null) {
          if (Looper.getMainLooper()==Looper.myLooper()){
              initToast(context, msg);
          }else {
              handler.post(new Runnable() {
                  @Override
                  public void run() {
                      initToast(context, msg);
                  }
              });
          }
        }
        //判断当前代码是否是主线程
        if (Looper.myLooper() == Looper.getMainLooper()) {
            sToast.setText(msg);
            sToast.show();
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    sToast.setText(msg);
                    sToast.show();
                }
            });
        }
    }

    private static void initToast(Context context, String msg) {
        sToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
        sToast.setText(msg);
    }
}
