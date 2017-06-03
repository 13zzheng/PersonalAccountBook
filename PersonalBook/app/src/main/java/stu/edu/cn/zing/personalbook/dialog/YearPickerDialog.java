package stu.edu.cn.zing.personalbook.dialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Date;

import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;

public class YearPickerDialog extends BaseActivity implements View.OnClickListener {

    private TextView tv_title;
    private DatePicker date_picker;
    private TextView tv_enter;

    private int year;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_year_picker_dialog);
    }

    @Override
    public void initViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        date_picker  = (DatePicker) findViewById(R.id.date_picker);
        tv_enter = (TextView) findViewById(R.id.tv_enter);
    }

    @Override
    public void initListeners() {
        tv_enter.setOnClickListener(this);
    }

    @Override
    public void initData() {
        setResult(ResultRequestCode.RESULT_NO);
        Intent intent = getIntent();
        year = intent.getExtras().getInt("year");
        date_picker.init(year, date_picker.getMonth(), date_picker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int y, int monthOfYear, int dayOfMonth) {
                year = y;
                tv_title.setText(year + "年");
            }
        });
        date_picker.setMaxDate(new Date().getTime());

        View monthPickerView = findViewById(getResources().getIdentifier("android:id/month", null, null));
        if(monthPickerView != null){
            monthPickerView.setVisibility(View.GONE);
        }

        View dayPickerView = findViewById(getResources().getIdentifier("android:id/day", null, null));
        if(dayPickerView != null){
            dayPickerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_enter:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt("year", year);
                intent.putExtras(bundle);
                setResult(ResultRequestCode.RESULT_YEAR_PICKER, intent);
                finish();
                break;
        }
    }
}
