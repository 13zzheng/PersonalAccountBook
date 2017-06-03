package stu.edu.cn.zing.personalbook.model;

import stu.edu.cn.zing.personalbook.FindListenerUI;
import stu.edu.cn.zing.personalbook.bmobclass.User;

/**
 * Created by Administrator on 2017/3/28.
 */

public interface IQuerier {

    public void queryCurrentItemType(User user, final FindListenerUI findListenerUI);
}
