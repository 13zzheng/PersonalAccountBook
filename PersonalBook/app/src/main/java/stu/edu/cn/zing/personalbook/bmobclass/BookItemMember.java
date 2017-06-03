package stu.edu.cn.zing.personalbook.bmobclass;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/28.
 */

public class BookItemMember extends BmobObject implements Serializable{
    private String itemMember;
    private User user;
    private Boolean isDefault = false;

    public String getItemMember() {
        return itemMember;
    }

    public void setItemMember(String itemMember) {
        this.itemMember = itemMember;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
