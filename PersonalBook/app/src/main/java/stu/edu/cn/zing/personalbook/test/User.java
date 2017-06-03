package stu.edu.cn.zing.personalbook.test;

/**
 * Created by Administrator on 2017/4/27.
 */

public class User implements IUser{

    @Override
    public boolean login(String account, String password) {
        //具体业务逻辑代码


        //这里返回是否登录成功
        return true;
    }

    @Override
    public boolean regist(String accout, String password, String username) {
        //具体业务逻辑代码


        //这里返回是否注册成功
        return false;
    }
}
