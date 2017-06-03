package stu.edu.cn.zing.personalbook.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.FindListenerUI;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.bmobclass.Book;
import stu.edu.cn.zing.personalbook.bmobclass.User;
import stu.edu.cn.zing.personalbook.bmobclass.BookMonthFinancial;
import stu.edu.cn.zing.personalbook.model.BookManager;

public class BudgetDialog extends BaseActivity implements View.OnClickListener,Switch.OnCheckedChangeListener{

    private Switch switch_budget;
    private EditText et_budget_value;
    private TextView tv_enter;

    private int month;
    private int year;

    private BookMonthFinancial bookMonthFinancial;
    private boolean isBudgetEnable = false;


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_budget_dialog);
    }

    @Override
    public void initViews() {
        switch_budget = (Switch) findViewById(R.id.switch_budget);
        et_budget_value = (EditText) findViewById(R.id.et_budget_value);
        tv_enter = (TextView) findViewById(R.id.tv_enter);
    }

    @Override
    public void initListeners() {
        tv_enter.setOnClickListener(this);
        switch_budget.setOnCheckedChangeListener(this);
    }

    @Override
    public void initData() {
        setResult(ResultRequestCode.RESULT_NO);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        month = bundle.getInt("month");
        year = bundle.getInt("year");

        BookManager bookManager = new BookManager();
        bookManager.queryBookMonthFinancial(year, month, Book.getCurrentBook(), new FindListenerUI<BookMonthFinancial>() {
            @Override
            public void onSucess(List<BookMonthFinancial> list) {
                if (list.size() > 0) {
                    //存在
                    bookMonthFinancial = list.get(0);

                    switch_budget.setChecked(bookMonthFinancial.getBudgetEnable());
                    et_budget_value.setText(String.valueOf(bookMonthFinancial.getBudgetValue()));
                }
            }

            @Override
            public void onFail() {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_enter:
                final Float value = Float.valueOf(et_budget_value.getText().toString());

                bookMonthFinancial.setBudgetEnable(isBudgetEnable);
                bookMonthFinancial.setBudgetValue(value);

                if (bookMonthFinancial.getBudgetEnable()) {
                    //如果预算设置开启

                    bookMonthFinancial.setBudgetOverageValue(bookMonthFinancial.getBudgetValue() - bookMonthFinancial.getMonthOutput());

                }

                bookMonthFinancial.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {

                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("bookMonthFinancial", bookMonthFinancial);
                            intent.putExtras(bundle);
                            setResult(ResultRequestCode.RESULT_BUDGET_SETTING_SUCCESS, intent);
                            finish();
                        }
                    }
                });

                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            //选中
            isBudgetEnable = true;
        } else {
            //未选中
            isBudgetEnable = false;
        }
    }
}
