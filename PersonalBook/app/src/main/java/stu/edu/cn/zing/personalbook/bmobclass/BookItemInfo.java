package stu.edu.cn.zing.personalbook.bmobclass;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/28.
 */

public class BookItemInfo extends BmobObject {
    private BookItemType itemType;
    private BookItemMember itemMember;
    private BookItemTradingWay itemTradingWay;
    private String remarks;

    public BookItemType getItemType() {
        return itemType;
    }

    public void setItemType(BookItemType itemType) {
        this.itemType = itemType;
    }

    public BookItemMember getItemMember() {
        return itemMember;
    }

    public void setItemMember(BookItemMember itemMember) {
        this.itemMember = itemMember;
    }

    public BookItemTradingWay getItemTradingWay() {
        return itemTradingWay;
    }

    public void setItemTradingWay(BookItemTradingWay itemTradingWay) {
        this.itemTradingWay = itemTradingWay;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
