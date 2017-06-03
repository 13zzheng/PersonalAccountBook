package stu.edu.cn.zing.personalbook.dialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;

public class TimePickerDialog extends BaseActivity implements View.OnClickListener {

    private TextView tv_title;
    private TextView tv_enter;
    private TimePicker time_picker;

    private int hour;
    private int minute;


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_time_picker_dialog);
    }

    @Override
    public void initViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_enter = (TextView) findViewById(R.id.tv_enter);
        time_picker = (TimePicker) findViewById(R.id.time_picker);


    }

    @Override
    public void initListeners() {
        tv_enter.setOnClickListener(this);

        time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int m) {
                hour = hourOfDay;
                minute = m;
                tv_title.setText("时间选择——" + hour + ":" + minute);
            }
        });
    }


    @Override
    public void initData() {
        setResult(ResultRequestCode.RESULT_NO);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            hour = bundle.getInt("hour");
            minute = bundle.getInt("minute");
            tv_title.setText("时间选择——" + hour + ":" + minute);
            time_picker.setHour(hour);
            time_picker.setMinute(minute);
        } else {
            hour = time_picker.getHour();
            minute = time_picker.getMinute();
        }

        time_picker.setIs24HourView(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_enter:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("hour", hour);
                bundle.putInt("minute", minute);
                intent.putExtras(bundle);
                setResult(ResultRequestCode.RESULT_TIME_PICKER, intent);
                finish();
                break;
        }
    }
}
