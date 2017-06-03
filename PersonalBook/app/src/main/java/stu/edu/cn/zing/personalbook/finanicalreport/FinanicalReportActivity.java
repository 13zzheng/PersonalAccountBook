package stu.edu.cn.zing.personalbook.finanicalreport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.R;

public class FinanicalReportActivity extends BaseActivity implements IFinanicalReportActivity {

    private TextView tv_title;
    private TextView tv_input_value;
    private TextView tv_output_value;
    private TextView tv_budget_value;
    private TextView tv_overage_value;
    private TextView tv_overage_percentage;
    private TextView tv_most_output_day;
    private TextView tv_most_output_day_payment;
    private TextView tv_day_average_input;
    private TextView tv_day_average_output;
    private TextView tv_output_rate_type;
    private TextView tv_output_rate_type_number;
    private TextView tv_output_rate_member;
    private TextView tv_output_rate_member_number;
    private TextView tv_output_payment_type;
    private TextView tv_output_payment_type_value;
    private TextView tv_output_payment_member;
    private TextView tv_output_payment_member_value;


    private LinearLayout ll_budget;

    private RelativeLayout rl_no_data;
    private RelativeLayout rl_data;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_finanical_report);
    }

    @Override
    public void initViews() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_input_value = (TextView) findViewById(R.id.tv_input_value);
        tv_output_value = (TextView) findViewById(R.id.tv_output_value);
        tv_budget_value = (TextView) findViewById(R.id.tv_budget_value);
        tv_overage_value = (TextView) findViewById(R.id.tv_overage_value);
        tv_overage_percentage = (TextView) findViewById(R.id.tv_overage_percentage);
        tv_most_output_day = (TextView) findViewById(R.id.tv_most_output_day);
        tv_most_output_day_payment = (TextView) findViewById(R.id.tv_most_output_day_payment);
        tv_day_average_input = (TextView) findViewById(R.id.tv_day_average_input);
        tv_day_average_output = (TextView) findViewById(R.id.tv_day_average_output);
        tv_output_rate_type = (TextView) findViewById(R.id.tv_output_rate_type);
        tv_output_rate_type_number = (TextView) findViewById(R.id.tv_output_rate_type_number);
        tv_output_rate_member = (TextView) findViewById(R.id.tv_output_rate_member);
        tv_output_rate_member_number = (TextView) findViewById(R.id.tv_output_rate_member_number);
        tv_output_payment_type = (TextView) findViewById(R.id.tv_output_payment_type);
        tv_output_payment_type_value = (TextView) findViewById(R.id.tv_output_payment_type_value);
        tv_output_payment_member = (TextView) findViewById(R.id.tv_output_payment_member);
        tv_output_payment_member_value = (TextView) findViewById(R.id.tv_output_payment_member_value);

        ll_budget = (LinearLayout) findViewById(R.id.ll_budget);

        rl_no_data = (RelativeLayout) findViewById(R.id.rl_no_data);
        rl_data = (RelativeLayout) findViewById(R.id.rl_data);

    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int year = bundle.getInt("year");
        int month = bundle.getInt("month");

        FinanicalReportData finanicalReportData = new FinanicalReportData(this);
        finanicalReportData.setDate(year, month);
        finanicalReportData.init();


    }

    @Override
    public void showData() {
        rl_data.setVisibility(View.VISIBLE);
        rl_no_data.setVisibility(View.GONE);
    }

    @Override
    public void showNoData() {
        rl_no_data.setVisibility(View.VISIBLE);
        rl_data.setVisibility(View.GONE);
    }

    private String decimal(float f) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(f);
    }


    @Override
    public void setTotalInput(float input) {
        tv_input_value.setText(String.valueOf(input));
    }

    @Override
    public void setTotalOutput(float output) {
        tv_output_value.setText(String.valueOf(output));
    }

    @Override
    public void setBudgetEnable(boolean b) {
        if (b) {
            ll_budget.setVisibility(View.VISIBLE);
        } else {
            ll_budget.setVisibility(View.GONE);
        }
    }

    @Override
    public void setBudgetPercentage(float percentage) {
        String p = decimal(Math.abs(percentage * 100));
        if (percentage >= 0) {
            tv_budget_value.setText("剩余" + p + "%");
            tv_budget_value.setTextColor(getResources().getColor(R.color.report_positive,null));
        } else {
            tv_budget_value.setText("超出" + p + "%");
            tv_budget_value.setTextColor(getResources().getColor(R.color.report_negative,null));
        }
    }


    @Override
    public void setOverageValue(float overageValue) {
        tv_overage_value.setText(overageValue + "");
        if (overageValue < 0 ) {
            tv_overage_value.setTextColor(getResources().getColor(R.color.report_negative,null));
        }
    }

    @Override
    public void setOveragePercentage(float overagePercentage) {
        String p = decimal(Math.abs(overagePercentage * 100));
        tv_overage_percentage.setText(p + "%");
        tv_overage_percentage.setTextColor(getResources().getColor(R.color.report_middle, null));
    }

    @Override
    public void setMostOutputDay(int month, int day) {
        tv_most_output_day.setText(month + "月" + day + "日");
    }

    @Override
    public void setMostOutputValue(float value) {
        tv_most_output_day_payment.setText(decimal(value));
    }

    @Override
    public void setAverageDayInput(float input) {
        tv_day_average_input.setText(decimal(input));
    }

    @Override
    public void setAverageDayOutput(float output) {
        tv_day_average_output.setText(decimal(output));
    }

    @Override
    public void setMostRateType(String type) {
        tv_output_rate_type.setText(type);
    }

    @Override
    public void setMostRateTypeNumber(int number) {
        tv_output_rate_type_number.setText(number + "");
    }

    @Override
    public void setMostRateMember(String member) {
        tv_output_rate_member.setText(member);
    }

    @Override
    public void setMostRateMemberNumber(int number) {
        tv_output_rate_member_number.setText(number + "");
    }

    @Override
    public void setMostPaymentType(String type) {
        tv_output_payment_type.setText(type);
    }

    @Override
    public void setMostPaymentTypeValue(float value) {
        tv_output_payment_type_value.setText(decimal(value));
    }

    @Override
    public void setMostPaymentMember(String member) {
        tv_output_payment_member.setText(member);
    }

    @Override
    public void setMostPaymentMemberValue(float value) {
        tv_output_payment_member_value.setText(decimal(value));
    }


}
