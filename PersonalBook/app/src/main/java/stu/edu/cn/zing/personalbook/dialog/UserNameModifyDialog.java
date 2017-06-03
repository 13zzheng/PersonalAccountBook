package stu.edu.cn.zing.personalbook.dialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.bmobclass.User;

public class UserNameModifyDialog extends BaseActivity implements View.OnClickListener{


    private TextView tv_enter;
    private EditText et_user_name;
    private User user;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_user_name_modify_dialog);
    }

    @Override
    public void initViews() {
        tv_enter = (TextView) findViewById(R.id.tv_enter);
        et_user_name = (EditText) findViewById(R.id.et_user_name);
    }

    @Override
    public void initListeners() {
        tv_enter.setOnClickListener(this);
    }

    @Override
    public void initData() {
        setResult(ResultRequestCode.RESULT_NO);
        user = BmobUser.getCurrentUser(User.class);

        et_user_name.setText(user.getName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_enter:
                if (TextUtils.isEmpty(et_user_name.getText())) {
                    Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                user.setName(et_user_name.getText().toString());
                user.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        setResult(ResultRequestCode.RESULT_USER_NAME_MODIFY);
                        finish();
                    }
                });
                break;
        }
    }
}
