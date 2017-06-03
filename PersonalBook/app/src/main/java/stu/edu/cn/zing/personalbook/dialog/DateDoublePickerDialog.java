package stu.edu.cn.zing.personalbook.dialog;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;

public class DateDoublePickerDialog extends BaseActivity implements View.OnClickListener, DatePicker.OnDateChangedListener {

    private DatePicker date_picker1;
    private DatePicker date_picker2;

    private TextView tv_title;
    private TextView tv_enter;

    private int startYear;
    private int startMonth;
    private int startDay;
    private int endYear;
    private int endMonth;
    private int endDay;

    private int request;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_date_double_picker_dialog);
    }

    @Override
    public void initViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_enter = (TextView) findViewById(R.id.tv_enter);
        date_picker1 = (DatePicker) findViewById(R.id.date_picker1);
        date_picker2 = (DatePicker) findViewById(R.id.date_picker2);

    }

    @Override
    public void initListeners() {
        tv_enter.setOnClickListener(this);
    }

    @Override
    public void initData() {
        setResult(ResultRequestCode.RESULT_NO);
        startYear = endYear = date_picker1.getYear();
        startMonth = endMonth = date_picker1.getMonth() + 1;
        startDay = endDay = date_picker1.getDayOfMonth();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        request = bundle.getInt("request");

        date_picker1.setMaxDate(new Date().getTime());
        date_picker1.init(startYear, startMonth, startDay, this);

        date_picker2.setMaxDate(new Date().getTime());
        date_picker2.init(endYear, endMonth ,endDay, this);

        if (request == ResultRequestCode.REQUEST_DATE_DOUBLE_PICKER) {
            tv_title.setText(startYear + "年" + startMonth + "月" + startDay + "日" + "-" +
                    endYear + "年" + endMonth + "月" + endDay + "日");
        } else if (request == ResultRequestCode.REQUEST_DATE_DOUBLE_PICKER_WITH_NO_DAY) {
            //去掉天数显示


            tv_title.setText("理财报告——" +startYear + "年" + startMonth + "月" + "-" +
                    endYear + "年" + endMonth + "月");
        }

    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        switch (view.getId()) {
            case R.id.date_picker1:
                startYear = year;
                startMonth = monthOfYear + 1;
                startDay = dayOfMonth;

                break;

            case R.id.date_picker2:
                endYear = year;
                endMonth = monthOfYear + 1;
                endDay = dayOfMonth;

                break;
        }
        if (request == ResultRequestCode.REQUEST_DATE_DOUBLE_PICKER) {
            tv_title.setText(startYear + "年" + startMonth + "月" + startDay + "日" + "-" +
                    endYear + "年" + endMonth + "月" + endDay + "日");
        } else if (request == ResultRequestCode.REQUEST_DATE_DOUBLE_PICKER_WITH_NO_DAY) {
            tv_title.setText("理财报告——" +startYear + "年" + startMonth + "月" + "-" +
                    endYear + "年" + endMonth + "月");
        }
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.tv_enter:
                if (startYear > endYear || startMonth > endMonth || startDay > endDay) {
                    Toast.makeText(this, "起始时间不能大于终止时间", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (request == ResultRequestCode.REQUEST_DATE_DOUBLE_PICKER) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("startYear", startYear);
                    bundle.putInt("startMonth", startMonth);
                    bundle.putInt("startDay", startDay);
                    bundle.putInt("endYear", endYear);
                    bundle.putInt("endMonth", endMonth);
                    bundle.putInt("endDay", endDay);
                    intent.putExtras(bundle);
                    setResult(ResultRequestCode.RESULT_DATE_DOUBLE_PICKER, intent);
                    finish();
                } else if (request == ResultRequestCode.REQUEST_DATE_DOUBLE_PICKER_WITH_NO_DAY) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("startYear", startYear);
                    bundle.putInt("startMonth", startMonth);
                    bundle.putInt("endYear", endYear);
                    bundle.putInt("endMonth", endMonth);
                    intent.putExtras(bundle);

                }


                break;
        }
    }
}
