package stu.edu.cn.zing.personalbook.user_regist;

import stu.edu.cn.zing.personalbook.model.IUserManager;
import stu.edu.cn.zing.personalbook.SaveListenerUI;
import stu.edu.cn.zing.personalbook.model.UserManager;
import stu.edu.cn.zing.personalbook.bmobclass.User;

/**
 * Created by Administrator on 2017/3/27.
 */

public class RegistPresenter {

    private IRegistView iRegistView;
    private IUserManager iUserManager;

    public RegistPresenter(IRegistView iRegistView) {
        this.iRegistView = iRegistView;
        iUserManager = new UserManager();
    }

    public void regist(User user) {
        iUserManager.regist(user, new SaveListenerUI() {
            @Override
            public void onSucessUI() {
                iRegistView.registSucess();
            }

            @Override
            public void onFailUI() {
                iRegistView.registFail();
            }
        });
    }
}
