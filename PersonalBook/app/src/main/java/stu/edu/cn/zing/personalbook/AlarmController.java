package stu.edu.cn.zing.personalbook;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/29.
 */

public class AlarmController {
    private AlarmManager alarmManager;
    private Context context;
    private PendingIntent pendingIntent;

    public AlarmController (Context context) {
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationService.class);
        pendingIntent = PendingIntent.getService(context, 0, intent, 0);
    }

    public void setAlarm(int hour, int minute) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH-mm");
        String d = simpleDateFormat.format(new Date());
        String[] ds = d.split("-");
        int h = Integer.valueOf(ds[0]);
        int m = Integer.valueOf(ds[1]);
        Log.i("NowDate",h + " " + m);
        int dif = (hour - h)*60 + minute - m;
        if (dif > 0) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + dif* 60*1000, pendingIntent);
        }else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 24*3600*1000 - dif * 60*1000, pendingIntent);
        }

    }

    public void cancel() {
        alarmManager.cancel(pendingIntent);
    }

    public void setAlarmRepeat(long endTime) {
        long nowTime = System.currentTimeMillis();
        if (endTime - nowTime >= 0) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, endTime - System.currentTimeMillis(), pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, endTime, 24*3600*1000, pendingIntent);
        } else {

        }
    }
}
