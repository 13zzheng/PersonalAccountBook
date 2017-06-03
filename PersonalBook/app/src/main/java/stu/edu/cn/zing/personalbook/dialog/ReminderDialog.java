package stu.edu.cn.zing.personalbook.dialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.bmobclass.BookReminder;
import stu.edu.cn.zing.personalbook.bmobclass.User;

public class ReminderDialog extends BaseActivity implements View.OnClickListener {


    private LinearLayout ll_time;
    private TextView tv_enter;
    private TextView tv_time;
    private Switch switch_enable;
    private Switch switch_repeat;

    private int hour;
    private int minute;

    private BookReminder bookReminder;


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_reminder);
    }

    @Override
    public void initViews() {
        ll_time = (LinearLayout) findViewById(R.id.ll_time);
        tv_enter = (TextView) findViewById(R.id.tv_enter);
        tv_time = (TextView) findViewById(R.id.tv_time);
        switch_enable = (Switch) findViewById(R.id.switch_enable);
        switch_repeat = (Switch) findViewById(R.id.switch_repeat);
    }

    @Override
    public void initListeners() {
        ll_time.setOnClickListener(this);
        tv_enter.setOnClickListener(this);

    }

    @Override
    public void initData() {
        setResult(ResultRequestCode.RESULT_NO);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            bookReminder = (BookReminder) bundle.getSerializable("reminder");
            hour = bookReminder.getHour();
            minute = bookReminder.getMinute();
            setTime();
            switch_enable.setChecked(bookReminder.getEnable());
            switch_repeat.setChecked(bookReminder.getRepeatable());
        }
    }

    private void setTime() {
        tv_time.setText(hour+ " " + formateTime(minute));
    }

    private String formateTime(int i) {
        if (i < 10) {
            return "0" + i;
        }else {
            return i + "";
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ResultRequestCode.RESULT_NO) return;

        switch (requestCode) {
            case ResultRequestCode.REQUEST_TIME_PICKER:
                if (resultCode == ResultRequestCode.RESULT_TIME_PICKER) {
                    Bundle bundle = data.getExtras();
                    hour = bundle.getInt("hour");
                    minute = bundle.getInt("minute");
                    setTime();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_time:
                Intent intent = new Intent(this, TimePickerDialog.class);
                Bundle bundle = new Bundle();
                bundle.putInt("hour", hour);
                bundle.putInt("minute",minute);
                intent.putExtras(bundle);
                startActivityForResult(intent, ResultRequestCode.REQUEST_TIME_PICKER);
                break;

            case R.id.tv_enter:
                if (TextUtils.isEmpty(tv_time.getText())){
                    Toast.makeText(this, "时间不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (bookReminder != null) {
                    bookReminder.setHour(hour);
                    bookReminder.setMinute(minute);
                    bookReminder.setEnable(switch_enable.isChecked());
                    bookReminder.setRepeatable(switch_repeat.isChecked());
                    bookReminder.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("reminder", bookReminder);
                            intent.putExtras(bundle);
                            setResult(ResultRequestCode.RESULT_TIME_PICKER, intent);

                            finish();
                        }
                    });
                } else {
                    bookReminder = new BookReminder();
                    bookReminder.setHour(hour);
                    bookReminder.setMinute(minute);
                    bookReminder.setEnable(switch_enable.isChecked());
                    bookReminder.setRepeatable(switch_repeat.isChecked());
                    bookReminder.setUser(BmobUser.getCurrentUser(User.class));
                    bookReminder.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("reminder", bookReminder);
                            intent.putExtras(bundle);
                            setResult(ResultRequestCode.RESULT_TIME_PICKER, intent);

                            finish();
                        }
                    });
                }
                break;
        }
    }
}
