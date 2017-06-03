package stu.edu.cn.zing.personalbook.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.bmobclass.BookItemType;
import stu.edu.cn.zing.personalbook.bmobclass.User;
import stu.edu.cn.zing.personalbook.mywidget.LineEditText;

/**
 * Created by Administrator on 2017/3/30.
 */

public class AddItemTypeDialog extends BaseActivity implements View.OnClickListener{

    private LineEditText et_itemtype;
    private TextView tv_save;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_add_book_item_type);
    }

    @Override
    public void initViews() {
        et_itemtype = (LineEditText) findViewById(R.id.et_item_type);
        tv_save = (TextView) findViewById(R.id.tv_add);
    }

    @Override
    public void initListeners() {
        tv_save.setOnClickListener(this);
    }

    @Override
    public void initData() {
        setResult(RESULT_OK);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                addItemType();
                break;
        }
    }

    private void addItemType() {
        if (TextUtils.isEmpty(et_itemtype.getText())) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String type = et_itemtype.getText().toString();
        final BookItemType bookItemType= new BookItemType(type);
        User user = BmobUser.getCurrentUser(User.class);
        bookItemType.setUser(user);
        bookItemType.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    //添加成功
                    setResult(ResultRequestCode.RESULT_ADD_ITEM_TYPE_SUCCESS);
                    finish();
                } else {
                    //添加失败
                    Log.e("add_item_type","添加失败");
                }
            }
        });
    }
}
