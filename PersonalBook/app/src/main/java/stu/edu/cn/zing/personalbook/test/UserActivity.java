package stu.edu.cn.zing.personalbook.test;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import stu.edu.cn.zing.personalbook.R;

public class UserActivity extends Activity implements IUserActivity {

    private Presenter presenter;

    private String account;
    private String password;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        init();
    }

    private void init() {
        //初始化数据
        presenter = new Presenter(this);
    }

    private void login() {
        //用户登录
        getLoginInfo();
        presenter.login(account, password);
    }

    private void regist() {
        //用户注册
        getRegistInfo();
        presenter.regist(account, password, username);
    }
    @Override
    public void showError(String message) {
        //更新UI
    }

    @Override
    public void loginSuccess() {
        //更新UI
    }

    @Override
    public void registSuccess() {
        //更新UI
    }


    private void getLoginInfo() {

    }

    private void getRegistInfo() {

    }
}
