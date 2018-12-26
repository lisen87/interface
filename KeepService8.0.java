package com.qiaotongtianxia.sevices;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

/**
 * Created by lisen on 2018-10-27.
 *
 * @author lisen < 453354858@qq.com >
 */

public class KeepService extends Service {
    public static final int NOTIFICATION_ID=0x11;

    public KeepService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //API 18以上，发送Notification并将其置为前台后，启动InnerService
        //如果API在26以上即版本为O则调用startForefround()方法启动服务
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            setForegroundService();
            startService(new Intent(this, InnerService.class));
        }else{

            Notification.Builder builder = new Notification.Builder(this);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            startForeground(NOTIFICATION_ID, builder.build());
            startService(new Intent(this, InnerService.class));
        }
    }

    /**
     * 当版本8.0以上时，开启前台服务的方法
     */
    @SuppressLint("NewApi")
    public void  setForegroundService() {
        //设定的通知渠道名称
        String channelName = "渠道名称";
        //设置通知的重要程度
        int importance = NotificationManager.IMPORTANCE_HIGH;
        //构建通知渠道
        NotificationChannel channel = new NotificationChannel("123", channelName, importance);
        //在创建的通知渠道上发送通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "123");
        builder.setSmallIcon(R.mipmap.ic_launcher) //设置通知图标
                .setContentTitle("affew")//设置通知标题
                .setContentText("设置通知内容")//设置通知内容
                .setAutoCancel(true) //用户触摸时，自动关闭
                .setOngoing(true);//设置处于运行状态
        //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService( Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
//        notificationManager.cancel(NOTIFICATION_ID);
        //将服务置于启动状态 NOTIFICATION_ID指的是创建的通知的ID
        startForeground(NOTIFICATION_ID,builder.build());
    }
    public  static class  InnerService extends Service{
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
        @Override
        public void onCreate() {
            super.onCreate();
            //发送与KeepLiveService中ID相同的Notification，然后将其取消并取消自己的前台显示
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                setForegroundService();
            }else{
                Notification.Builder builder = new Notification.Builder(this);
                builder.setSmallIcon(R.mipmap.ic_launcher);
                startForeground(NOTIFICATION_ID, builder.build());
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopForeground(true);
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.cancel(NOTIFICATION_ID);
                    stopSelf();
                }
            },100);

        }
        @SuppressLint("NewApi")
        public void  setForegroundService() {
            //设定的通知渠道名称
            String channelName = "渠道名称";
            //设置通知的重要程度
            int importance = NotificationManager.IMPORTANCE_HIGH;
            //构建通知渠道
            NotificationChannel channel = new NotificationChannel("123", channelName, importance);
            //在创建的通知渠道上发送通知
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "123");
            builder.setSmallIcon(R.mipmap.ic_launcher) //设置通知图标
                    .setContentTitle("affew")//设置通知标题
                    .setContentText("设置通知内容")//设置通知内容
                    .setAutoCancel(true) //用户触摸时，自动关闭
                    .setOngoing(true);//设置处于运行状态
            //向系统注册通知渠道，注册后不能改变重要性以及其他通知行为
            NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService( Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            //将服务置于启动状态 NOTIFICATION_ID指的是创建的通知的ID
            startForeground(NOTIFICATION_ID,builder.build());
        }
    }
}
