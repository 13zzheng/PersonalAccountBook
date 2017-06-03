package stu.edu.cn.zing.personalbook.dialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.bmobclass.User;

public class PasswordModifyDialog extends BaseActivity implements View.OnClickListener{

    private EditText et_password1;
    private EditText et_password2;
    private TextView tv_enter;

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_password_modify_dialog);
    }

    @Override
    public void initViews() {
        et_password1 = (EditText) findViewById(R.id.et_password1);
        et_password2 = (EditText) findViewById(R.id.et_password2);
        tv_enter = (TextView) findViewById(R.id.tv_enter);
    }

    @Override
    public void initListeners() {
        tv_enter.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_enter:
                if (TextUtils.isEmpty(et_password1.getText()) || TextUtils.isEmpty(et_password2.getText())) {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                User user = BmobUser.getCurrentUser(User.class);
                User newUser = new User();
                newUser.setUsername(user.getUsername());
                newUser.setPassword(et_password1.getText().toString());
                newUser.login(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            user.setPassword(et_password2.getText().toString());
                            user.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(PasswordModifyDialog.this, "密码修改成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            });
                        }else  {
                            Toast.makeText(PasswordModifyDialog.this, "密码修改失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }
}
