package stu.edu.cn.zing.personalbook.dialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.model.PaymentFinder;

public class PaymentPickerDialog extends BaseActivity implements CheckBox.OnCheckedChangeListener,View.OnClickListener {


    private CheckBox cb_output;
    private CheckBox cb_input;
    private CheckBox cb_less;
    private CheckBox cb_great;

    private EditText et_less_value;
    private EditText et_great_value;

    private TextView tv_enter;

    private PaymentFinder paymentFinder;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_payment_picker_dialog);
    }

    @Override
    public void initViews() {
        cb_output = (CheckBox) findViewById(R.id.cb_output);
        cb_input = (CheckBox) findViewById(R.id.cb_input);
        cb_less = (CheckBox) findViewById(R.id.cb_less);
        cb_great = (CheckBox) findViewById(R.id.cb_great);

        et_less_value = (EditText) findViewById(R.id.et_less_value);
        et_great_value = (EditText) findViewById(R.id.et_great_value);

        tv_enter = (TextView) findViewById(R.id.tv_enter);
    }

    @Override
    public void initListeners() {
        cb_output.setOnCheckedChangeListener(this);
        cb_input.setOnCheckedChangeListener(this);
        cb_less.setOnCheckedChangeListener(this);
        cb_great.setOnCheckedChangeListener(this);

        tv_enter.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        paymentFinder = (PaymentFinder) intent.getExtras().getSerializable("paymentFinder");
        setResult(ResultRequestCode.RESULT_NO);

        cb_great.setChecked(paymentFinder.isGreat());
        cb_less.setChecked(paymentFinder.isLess());
        cb_input.setChecked(paymentFinder.isInput());
        cb_output.setChecked(paymentFinder.isOutput());
        et_great_value.setText(String.valueOf(paymentFinder.getGreatValue()));
        et_less_value.setText(String.valueOf(paymentFinder.getLessValue()));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_output:
                paymentFinder.setOutput(isChecked);
                break;
            case R.id.cb_input:
                paymentFinder.setInput(isChecked);
                break;
            case R.id.cb_less:
                paymentFinder.setLess(isChecked);
                break;
            case R.id.cb_great:
                paymentFinder.setGreat(isChecked);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_enter:
                if (!TextUtils.isEmpty(et_less_value.getText())) {
                    paymentFinder.setLessValue(Float.valueOf(et_less_value.getText().toString()));
                }
                if (!TextUtils.isEmpty(et_great_value.getText())) {
                    paymentFinder.setGreatValue(Float.valueOf(et_great_value.getText().toString()));
                }
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                paymentFinder.setEnable(true);
                bundle.putSerializable("paymentFinder", paymentFinder);
                intent.putExtras(bundle);
                setResult(ResultRequestCode.RESULT_PAYMENT_PICKER,intent);
                finish();
                break;
        }
    }
}
