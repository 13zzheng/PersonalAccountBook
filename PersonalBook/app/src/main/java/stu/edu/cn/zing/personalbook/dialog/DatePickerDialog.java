package stu.edu.cn.zing.personalbook.dialog;


import android.content.Intent;
import android.support.annotation.Dimension;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.finanicalreport.FinanicalReportActivity;

public class DatePickerDialog extends BaseActivity implements View.OnClickListener,DatePicker.OnDateChangedListener {

    private TextView tv_title;
    private TextView tv_enter;
    private DatePicker date_picker;

    private int request;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_date_picker_dialog);
    }

    @Override
    public void initViews() {

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_enter = (TextView) findViewById(R.id.tv_enter);
        date_picker = (DatePicker) findViewById(R.id.date_picker);


    }

    @Override
    public void initListeners() {
        tv_enter.setOnClickListener(this);
    }

    @Override
    public void initData() {
        setResult(ResultRequestCode.RESULT_NO);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            request = bundle.getInt("request");
        }
        switch (request) {
            case ResultRequestCode.REQUEST_DATE_PICKER_WITH_NO_DAY:
                //如果访问的是不带天数的
                //去掉天数显示
                int year = intent.getExtras().getInt("year");
                int month = intent.getExtras().getInt("month");
                View dayPickerView = findViewById(getResources().getIdentifier("android:id/day", null, null));
                if(dayPickerView != null){
                    dayPickerView.setVisibility(View.GONE);
                }
                tv_title.setText(date_picker.getYear() + "年" + (date_picker.getMonth() + 1) + "月");
                date_picker.init(year, month, date_picker.getDayOfMonth(), this);
                break;

            case ResultRequestCode.REQUEST_DATE_PICKER:
                tv_title.setText(date_picker.getYear() + "年" + (date_picker.getMonth() + 1) + "月" + date_picker.getDayOfMonth() + "日");
                break;

            case ResultRequestCode.REQUEST_DATE_PICKER_WITH_REPORT:
                View dayPickerView2 = findViewById(getResources().getIdentifier("android:id/day", null, null));
                if(dayPickerView2 != null){
                    dayPickerView2.setVisibility(View.GONE);
                }
                tv_title.setText("生成理财报告——" + date_picker.getYear() + "年" + (date_picker.getMonth() + 1) + "月");
                date_picker.init(date_picker.getYear(), date_picker.getMonth() + 1, date_picker.getDayOfMonth(), this);
                break;
        }



        date_picker.setMaxDate(new Date().getTime());
        //date_picker.init(date_picker.getYear(), date_picker.getMonth() + 1, date_picker.getDayOfMonth(), this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_enter:
                Intent data = new Intent();
                Bundle bundle = new Bundle();
                switch (request) {
                    case ResultRequestCode.REQUEST_DATE_PICKER_WITH_NO_DAY:
                        bundle.putInt("year", date_picker.getYear());
                        bundle.putInt("month", date_picker.getMonth() + 1);

                        data.putExtras(bundle);
                        setResult(ResultRequestCode.RESULT_DATE_PICKER,data);
                        finish();
                        break;

                    case ResultRequestCode.REQUEST_DATE_PICKER:
                        String year = String.valueOf(date_picker.getYear());
                        String month = String.valueOf(date_picker.getMonth() + 1);
                        if (month.length() == 1) {
                            month = "0" + month;
                        }
                        String day = String.valueOf(date_picker.getDayOfMonth());
                        if (day.length() == 1) {
                            day = "0" + day;
                        }
                        String date = year + "-" + month + "-" + day;
                        String week = getWeekByDate(date);
                        Log.i("Week",week);
                        bundle.putString("year", year);
                        bundle.putString("month", month);
                        bundle.putString("day", day);
                        bundle.putString("week", week);

                        data.putExtras(bundle);
                        setResult(ResultRequestCode.RESULT_DATE_PICKER,data);
                        finish();
                        break;

                    case ResultRequestCode.REQUEST_DATE_PICKER_WITH_REPORT:
                        bundle.putInt("year", date_picker.getYear());
                        bundle.putInt("month", date_picker.getMonth() + 1);

                        Intent intent = new Intent(this, FinanicalReportActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                }
                break;
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        switch (request) {
            case ResultRequestCode.REQUEST_DATE_PICKER_WITH_NO_DAY:
                tv_title.setText(year + "年" + (monthOfYear + 1) + "月");
                break;
            case ResultRequestCode.REQUEST_DATE_PICKER:
                tv_title.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");
                break;
            case ResultRequestCode.REQUEST_DATE_PICKER_WITH_REPORT:
                tv_title.setText("生成理财报告——" + year + "年" + (monthOfYear + 1) + "月");
                break;
        }
    }

    private String getWeekByDate(String date) {
        String week = "周";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            c.setTime(format.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            week += "日";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            week += "六";
        }

        return week;
    }
}
