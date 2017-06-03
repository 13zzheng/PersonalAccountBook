package stu.edu.cn.zing.personalbook.user_regist;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.HomeFragments.HomeActivity;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.bmobclass.User;
import stu.edu.cn.zing.personalbook.mywidget.LineEditText;

/**
 * Created by Administrator on 2017/3/23.
 */

public class RegistActivity extends BaseActivity implements View.OnClickListener,TextWatcher,IRegistView {
    private LineEditText et_account;
    private LineEditText et_password;
    private LineEditText et_password_confirmation;
    private LineEditText et_username;
    private TextInputLayout wrapper_account;
    private TextInputLayout wrapper_password;
    private TextInputLayout wrapper_password_confirmation;
    private TextInputLayout wrapper_username;
    private TextView tv_regist;


    private RegistPresenter registPresenter;


    @Override
    public void setContentView() {
        setContentView(R.layout.activity_regist);
    }

    @Override
    public void initViews() {
        et_account = (LineEditText) findViewById(R.id.et_account);
        et_password = (LineEditText) findViewById(R.id.et_password);
        et_password_confirmation = (LineEditText) findViewById(R.id.et_password_confirmation);
        et_username = (LineEditText) findViewById(R.id.et_username);
        wrapper_account = (TextInputLayout) findViewById(R.id.wrapper_account);
        wrapper_password = (TextInputLayout) findViewById(R.id.wrapper_password);
        wrapper_password_confirmation = (TextInputLayout) findViewById(R.id.wrapper_password_confirmation);
        wrapper_username = (TextInputLayout) findViewById(R.id.wrapper_username);
        tv_regist = (TextView) findViewById(R.id.tv_regist);
    }

    @Override
    public void initListeners() {
        tv_regist.setOnClickListener(this);
        et_account.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        et_password_confirmation.addTextChangedListener(this);
        et_username.addTextChangedListener(this);
    }

    @Override
    public void initData() {
        registPresenter = new RegistPresenter(this);
    }

    @Override
    public void onClick(View v) {

        if (v == tv_regist) {
            regist();
        }
    }

    private void regist() {
        String account = et_account.getText().toString();
        String rePassword = et_password_confirmation.getText().toString();
        String username = et_username.getText().toString();
        User user = new User();
        user.setUsername(account);
        user.setPassword(rePassword);
        user.setName(username);
        registPresenter.regist(user);

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String account = et_account.getText().toString();
        String password = et_password.getText().toString();
        String rePassword = et_password_confirmation.getText().toString();
        String username = et_username.getText().toString();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password) &&
                !TextUtils.isEmpty(rePassword) && !TextUtils.isEmpty(username) &&
                !isTextError) {
            tv_regist.setTextColor(getResources().getColor(R.color.colorPrimary));
            tv_regist.setClickable(true);
        } else {
            tv_regist.setTextColor(getResources().getColor(R.color.colorPrimary_tran));
            tv_regist.setClickable(false);
        }
    }

    private boolean temp1 = true;
    private boolean temp2 = true;
    private boolean temp3 = true;
    private boolean temp4 = true;
    private boolean isTextError = false;
    @Override
    public void afterTextChanged(Editable s) {
        String account = et_account.getText().toString();
        String password = et_password.getText().toString();
        String rePassword = et_password_confirmation.getText().toString();
        String username = et_username.getText().toString();

        if (account.contains(" ")) {
            if (temp1) {
                wrapper_account.setError("用户名不能包含空格！");
            }
            temp1 = false;
        } else {
            wrapper_account.setErrorEnabled(false);
            temp1 = true;
        }

        if (password.length() < 6 && !TextUtils.isEmpty(password)) {
            if (temp2) {
                wrapper_password.setError("密码至少6位数！");
            }
            temp2 = false;
        } else {
            wrapper_password.setErrorEnabled(false);
            temp2 = true;
        }

        if (!rePassword.equals(password) && !TextUtils.isEmpty(rePassword)) {
            if (temp3) {
                wrapper_password_confirmation.setError("密码确认错误！");
            }
            temp3 = false;
        } else {
            wrapper_password_confirmation.setErrorEnabled(false);
            temp3 = true;
        }

        if (username.contains(" ")) {
            if (temp4) {
                wrapper_username.setError("用户昵称不能包含空格！");
            }
            temp4 =false;
        } else {
            wrapper_username.setErrorEnabled(false);
            temp4 = true;
        }

        if (temp1 && temp2 && temp3 && temp4) {
            isTextError = false;
        } else {
            isTextError = true;
        }
    }

    @Override
    public void registSucess() {
        startActivity(new Intent(RegistActivity.this, HomeActivity.class));
        setResult(ResultRequestCode.RESULT_REGIST_SUCCESS);
        finish();
    }

    @Override
    public void registFail() {
        Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
        Log.e("regist","注册失败");
    }
}
