package stu.edu.cn.zing.personalbook.bmobclass;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/28.
 */

public class BookItemTradingWay extends BmobObject{

    private String itemTradingWay;
    private User user;
    private Boolean isDefault = false;

    public String getItemTradingWay() {
        return itemTradingWay;
    }

    public void setItemTradingWay(String itemTradingWay) {
        this.itemTradingWay = itemTradingWay;
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
