package stu.edu.cn.zing.personalbook.test;

/**
 * Created by Administrator on 2017/4/27.
 */

public class Presenter {

    private IUserActivity iUserActivity;
    private IUser iUser;
    public Presenter(IUserActivity iUserActivity) {
        this.iUserActivity = iUserActivity;
        iUser = new User();
    }

    public void login(String account, String password) {

        boolean b = iUser.login(account,password);
        if (b) {
            //登录成功
            iUserActivity.loginSuccess();
        }else {
            //登录失败
            iUserActivity.showError("登录失败");
        }
    }

    public void regist(String account, String password, String username) {

        boolean b = iUser.regist(account,password,username);
        if (b) {
            //注册成功
            iUserActivity.registSuccess();
        }else {
            //登录失败
            iUserActivity.showError("注册失败");
        }
    }
}
