package stu.edu.cn.zing.personalbook.bmobclass;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/23.
 */

public class BookItemType extends BmobObject implements Serializable {

    private String type;
    private User user;
    private Boolean isDefault = false;

    public BookItemType(String type) {
        this.type = type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
