package stu.edu.cn.zing.personalbook.model;

import stu.edu.cn.zing.personalbook.SaveListenerUI;
import stu.edu.cn.zing.personalbook.bmobclass.User;

/**
 * Created by Administrator on 2017/3/27.
 */

public interface IUserManager {
    public void login(User user, final SaveListenerUI saveListenerUI);
    public void regist(User user, final SaveListenerUI saveListenerUI);
}
