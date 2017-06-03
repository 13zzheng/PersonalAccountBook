package stu.edu.cn.zing.personalbook.dialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.model.StatisticalType;

public class StatisticalTypePickerDialog extends BaseActivity implements View.OnClickListener{

    private TextView tv_type;
    private TextView tv_member;
    private TextView tv_trading_way;


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_statistical_type_picker_dialog);
    }

    @Override
    public void initViews() {
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_member = (TextView) findViewById(R.id.tv_member);
        tv_trading_way = (TextView) findViewById(R.id.tv_trading_way);
    }

    @Override
    public void initListeners() {
        tv_type.setOnClickListener(this);
        tv_member.setOnClickListener(this);
        tv_trading_way.setOnClickListener(this);
    }

    @Override
    public void initData() {
        setResult(ResultRequestCode.RESULT_NO);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.tv_type:
                bundle.putInt("type", StatisticalType.TYPE);
                break;

            case R.id.tv_member:
                bundle.putInt("type", StatisticalType.MEMBER);
                break;

            case R.id.tv_trading_way:
                bundle.putInt("type", StatisticalType.TRADING_WAY);
                break;
        }
        intent.putExtras(bundle);
        setResult(ResultRequestCode.RESULT_STATISTICAL_TYPE_PICKER, intent);
        finish();
    }
}
