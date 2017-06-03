package stu.edu.cn.zing.personalbook.user_login;

import stu.edu.cn.zing.personalbook.model.IUserManager;
import stu.edu.cn.zing.personalbook.SaveListenerUI;
import stu.edu.cn.zing.personalbook.model.UserManager;
import stu.edu.cn.zing.personalbook.bmobclass.User;

/**
 * Created by Administrator on 2017/3/27.
 */

public class LoginPresenter {

    private ILoginView iLoginView;
    private IUserManager iUserManager;

    public LoginPresenter(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
        iUserManager = new UserManager();
    }

    public void login(User user) {
        iUserManager.login(user, new SaveListenerUI() {
            @Override
            public void onSucessUI() {
                iLoginView.loginSucess();
            }

            @Override
            public void onFailUI() {
                iLoginView.loginFail();
            }
        });
    }
}
