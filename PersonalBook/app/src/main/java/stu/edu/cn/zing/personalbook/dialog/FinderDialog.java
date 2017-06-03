package stu.edu.cn.zing.personalbook.dialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.listadapter.BookItemFinderListAdapter;
import stu.edu.cn.zing.personalbook.model.Finder;

public class FinderDialog extends BaseActivity implements View.OnClickListener {

    private ListView lv_type;
    private Finder finder;
    private TextView tv_enter;
    private int request;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_type_picker_dialog);
    }

    @Override
    public void initViews() {
        lv_type = (ListView) findViewById(R.id.lv_type);
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
        finder = (Finder) intent.getExtras().getSerializable("finder");
        request = intent.getExtras().getInt("request");
        BookItemFinderListAdapter bookItemFinderListAdapter = new BookItemFinderListAdapter(this, finder);
        lv_type.setAdapter(bookItemFinderListAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_enter:
                Intent intent1 = new Intent();
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("finder", finder);
                intent1.putExtras(bundle1);

                switch (request) {
                    case ResultRequestCode.RESULT_TYPE_PICKER:
                        setResult(ResultRequestCode.RESULT_TYPE_PICKER, intent1);
                        break;
                    case ResultRequestCode.RESULT_MEMBER_PICKER:
                        setResult(ResultRequestCode.RESULT_MEMBER_PICKER, intent1);
                        break;
                    case ResultRequestCode.RESULT_TRADING_WAY_PICKER:
                        setResult(ResultRequestCode.RESULT_TRADING_WAY_PICKER, intent1);
                        break;
                }
                finish();
                break;
        }
    }
}
