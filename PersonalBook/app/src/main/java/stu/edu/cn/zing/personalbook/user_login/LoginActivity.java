package stu.edu.cn.zing.personalbook.user_login;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import stu.edu.cn.zing.personalbook.BaseActivity;
import stu.edu.cn.zing.personalbook.HomeFragments.HomeActivity;
import stu.edu.cn.zing.personalbook.R;
import stu.edu.cn.zing.personalbook.ResultRequestCode;
import stu.edu.cn.zing.personalbook.bmobclass.User;
import stu.edu.cn.zing.personalbook.user_regist.RegistActivity;
import stu.edu.cn.zing.personalbook.mywidget.LineEditText;

/**
 * Created by Administrator on 2017/3/15.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener,ILoginView {

    private Button btn_login;
    private LineEditText et_account;
    private LineEditText et_password;
    private TextView tv_regist;

    private LoginPresenter loginPresenter;

    private int requestCode;
    @Override
    public void setContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initViews() {
        btn_login = (Button) findViewById(R.id.btn_login);
        et_account = (LineEditText) findViewById(R.id.et_account);
        et_password = (LineEditText) findViewById(R.id.et_password);
        tv_regist = (TextView) findViewById(R.id.tv_regist);

    }

    @Override
    public void initListeners() {
        btn_login.setOnClickListener(this);
        tv_regist.setOnClickListener(this);
    }

    @Override
    public void initData() {
        loginPresenter = new LoginPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;

            case R.id.tv_regist :
                toRegistActivity();
                break;

        }
    }

    private void login() {
        String account = et_account.getText().toString();
        String password = et_password.getText().toString();

        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();

            return;
        }
        User user = new User();
        user.setUsername(account);
        user.setPassword(password);
        loginPresenter.login(user);

    }

    private void toRegistActivity(){
        requestCode = 0;
        startActivityForResult(new Intent(LoginActivity.this, RegistActivity.class),requestCode);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ResultRequestCode.RESULT_REGIST_SUCCESS :
                finish();
                break;
        }
    }

    @Override
    public void loginSucess() {
        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
        finish();
    }

    @Override
    public void loginFail() {
        Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();

        Log.e("login","登录失败");
    }


}
