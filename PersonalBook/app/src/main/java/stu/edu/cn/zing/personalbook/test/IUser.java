package stu.edu.cn.zing.personalbook.test;

/**
 * Created by Administrator on 2017/4/27.
 */

public interface IUser {
    boolean login(String account, String password);
    boolean regist(String accout, String password, String username);
}
