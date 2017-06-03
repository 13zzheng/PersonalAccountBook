package stu.edu.cn.zing.personalbook.bmobclass;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/3/23.
 */

public class BookItem extends BmobObject implements Serializable {

    private BookItemInfo itemInfo;
    private BookPayment payment;
    private BookItemTitle itemTitle;

    public BookItemInfo getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(BookItemInfo itemInfo) {
        this.itemInfo = itemInfo;
    }

    public BookPayment getPayment() {
        return payment;
    }

    public void setPayment(BookPayment payment) {
        this.payment = payment;
    }

    public BookItemTitle getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(BookItemTitle itemTitle) {
        this.itemTitle = itemTitle;
    }

}
