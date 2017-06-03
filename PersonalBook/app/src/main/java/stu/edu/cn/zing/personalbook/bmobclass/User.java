package stu.edu.cn.zing.personalbook.bmobclass;

import android.app.Application;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/3/24.
 */

public class User extends BmobUser {

    private String name;
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
