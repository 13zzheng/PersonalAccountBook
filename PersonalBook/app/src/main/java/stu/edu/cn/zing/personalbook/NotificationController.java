package stu.edu.cn.zing.personalbook;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;


import stu.edu.cn.zing.personalbook.HomeFragments.HomeActivity;

/**
 * Created by Administrator on 2017/4/29.
 */

public class NotificationController {
    private NotificationManager notificationManager;
    private Context context;

    public NotificationController(NotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    public NotificationController (Context context) {
        this.context = context;
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    }


    public void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        //通知显示图标
        builder.setSmallIcon(R.mipmap.icon_add);
        //状态栏显示的通知文本
        builder.setTicker("收到一个新的通知");
        //标题栏
        builder.setContentTitle("记账提醒");
        //内容
        builder.setContentText("您的记账时间到了，点击开始记账。");
        //发送时间
        builder.setWhen(System.currentTimeMillis());
        //默认提示音
        builder.setDefaults(Notification.DEFAULT_ALL);
        //点击后图标消失
        builder.setAutoCancel(true);
        //设置点击跳转界面
        Intent intent = new Intent(context, HomeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
        builder.setContentIntent(contentIntent);

        Notification notification = builder.build();
        //发送通知
        notificationManager.notify(124,notification);

    }
}
